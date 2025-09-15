package archives.tater.endothermic.state

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.payload.EndResetPayload
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.PersistentState
import net.minecraft.world.PersistentStateType
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World

class EndResetState(resetTicks: Int = -1) : PersistentState() {
    var resetTicks = resetTicks
        private set
    

    companion object Manager : ServerTickEvents.EndWorldTick, UseItemCallback {

        val CODEC: Codec<EndResetState> = RecordCodecBuilder.create { it.group(
            Codec.INT.fieldOf("reset_ticks").forGetter(EndResetState::resetTicks)
        ).apply(it, ::EndResetState) }

        val TYPE = PersistentStateType("${Endothermic.MOD_ID}_end_reset", ::EndResetState, CODEC, null)

        const val DURATION = 300

        fun startReset(world: ServerWorld) {
            world.persistentStateManager.getOrCreate(TYPE).resetTicks = 0
            for (player in world.players.toList()) {
                ServerPlayNetworking.send(player, EndResetPayload)
            }
        }

        fun resetEnd(world: ServerWorld) {
            for (player in world.players.toList()) {
                player.teleportTo(player.getRespawnTarget(false, TeleportTarget.NO_OP))
            }
//            ((world.chunkManager.chunkLoadingManager.worker as StorageIoWorker).storageKey as StorageKeyAccessor).directory
        }

        override fun onEndTick(world: ServerWorld) {
            world.persistentStateManager.getOrCreate(TYPE).apply {
                if (resetTicks >= DURATION) {
                    resetEnd(world)
                    resetTicks = -1
                } else if (resetTicks >= 0)
                    resetTicks++
            }
        }

        override fun interact(
            player: PlayerEntity,
            world: World,
            hand: Hand
        ): ActionResult {
            if (!player.getStackInHand(hand).isOf(Items.NETHER_STAR)) return ActionResult.PASS
            if (world !is ServerWorld) return ActionResult.SUCCESS
            startReset(world)
            return ActionResult.SUCCESS
        }
    }
}