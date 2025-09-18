package archives.tater.endothermic.state

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.duck.ResettableWorld
import archives.tater.endothermic.entity.FloatingDragonEggEntity
import archives.tater.endothermic.payload.EndResetPayload
import archives.tater.endothermic.registry.EndothermicEntities
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.dragon.EnderDragonFight
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState
import net.minecraft.world.PersistentStateType
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World

class EndResetState(resetTicks: Int = -1) : PersistentState() {
    var resetTicks = resetTicks
        private set
    
    companion object Manager : ServerTickEvents.EndWorldTick, ServerLivingEntityEvents.AllowDamage {

        val CODEC: Codec<EndResetState> = RecordCodecBuilder.create { it.group(
            Codec.INT.fieldOf("reset_ticks").forGetter(EndResetState::resetTicks)
        ).apply(it, ::EndResetState) }

        val TYPE = PersistentStateType("${Endothermic.MOD_ID}_end_reset", ::EndResetState, CODEC, null)

        const val DURATION = 300
        const val DELAY = FloatingDragonEggEntity.ASCEND_TIME + 40

        @JvmStatic
        fun spawnEggEntity(world: World, pos: BlockPos, crystals: List<EndCrystalEntity>) {
            world.spawnEntity(FloatingDragonEggEntity(world, pos, crystals))
            for (crystal in crystals)
                crystal.isInvulnerable = true
        }

        @JvmStatic
        fun startReset(world: ServerWorld) {
            world.persistentStateManager.getOrCreate(TYPE).resetTicks = 0
        }

        fun resetEnd(world: ServerWorld) {
            for (entity in world.iterateEntities()) {
                if (!entity.isLiving || entity.type.isIn(EndothermicEntities.END_NATIVE)) continue
                if (entity is MobEntity && !entity.isPersistent && !entity.cannotDespawn()) continue
                if (entity is ServerPlayerEntity) {
                    entity.teleportTo(entity.getRespawnTarget(false, TeleportTarget.NO_OP))
                    continue
                }
                val overworld = world.server.overworld
                val pos = entity.getWorldSpawnPos(overworld, overworld.spawnPos).toBottomCenterPos()
                entity.teleport(overworld, pos.x, pos.y, pos.z, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), overworld.spawnAngle, 0f, true)
            }
            world.server.saveProperties.dragonFight = EnderDragonFight.Data.DEFAULT
            (world.server as ResettableWorld).`endothermic$reset`(world.registryKey)
        }

        override fun onEndTick(world: ServerWorld) {
            world.persistentStateManager.getOrCreate(TYPE).apply {
                if (resetTicks == DELAY)
                    for (player in world.players.toList()) {
                        ServerPlayNetworking.send(player, EndResetPayload)
                    }
                if (resetTicks >= DELAY + DURATION) {
                    resetEnd(world)
                    resetTicks = -1
                }
                if (resetTicks >= 0)
                    resetTicks++
            }
        }

        override fun allowDamage(
            entity: LivingEntity,
            source: DamageSource,
            amount: Float
        ): Boolean =
            entity.world.registryKey != World.END
                    || (entity.world as ServerWorld).persistentStateManager.getOrCreate(TYPE).resetTicks < DELAY
    }
}