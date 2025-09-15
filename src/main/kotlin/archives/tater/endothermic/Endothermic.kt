package archives.tater.endothermic

import archives.tater.endothermic.mixin.EnderDragonFightAccessor
import archives.tater.endothermic.registry.*
import archives.tater.endothermic.state.EndResetState
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory


object Endothermic : ModInitializer {
    const val MOD_ID = "endothermic"

    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

    @JvmField
    val logger = LoggerFactory.getLogger(MOD_ID)

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
        EndothermicEntities.init()
        registerPayloads()

        ServerTickEvents.END_WORLD_TICK.register(EndResetState)
        UseItemCallback.EVENT.register(EndResetState)
	}
}