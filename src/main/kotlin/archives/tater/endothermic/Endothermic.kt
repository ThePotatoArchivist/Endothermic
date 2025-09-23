package archives.tater.endothermic

import archives.tater.endothermic.mixin.EnderDragonFightAccessor
import archives.tater.endothermic.registry.*
import archives.tater.endothermic.state.EndResetState
import archives.tater.endothermic.state.EndResetState.Manager.startReset
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.item.Items
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.slf4j.LoggerFactory


object Endothermic : ModInitializer {
    const val MOD_ID = "endothermic"

    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

    @JvmField
    val logger = LoggerFactory.getLogger(MOD_ID)

    const val END_CRYSTALS_BOSSBAR = "bossbar.endothermic.end_crystals"

    @JvmStatic
    fun isDragonKilled(world: ServerWorld) = (world.enderDragonFight as EnderDragonFightAccessor?)?.dragonKilled ?: false

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        EndothermicDamageTypes.init()
        EndothermicDataAttachments.init()
        EndothermicParticles.init()
        EndothermicItems.init()
        EndothermicBlocks.init()
        EndothermicTrackedDataHandlers.init()
        EndothermicEntities.init()
        EndothermicEnchantments.init()
        EndothermicWorldgen.init()
        registerPayloads()

        ServerTickEvents.END_WORLD_TICK.register(EndResetState)
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(EndResetState)
        if (FabricLoader.getInstance().isDevelopmentEnvironment)
            UseItemCallback.EVENT.register { player, world, hand -> when {
                !player.getStackInHand(hand).isOf(Items.NETHER_STAR) || world.registryKey != World.END -> ActionResult.PASS
                world !is ServerWorld -> ActionResult.SUCCESS
                else -> {
                    startReset(world)
                    ActionResult.SUCCESS
                }
            } }
	}
}