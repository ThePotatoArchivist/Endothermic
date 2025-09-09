package archives.tater.endothermic

import archives.tater.endothermic.registry.EndothermicDamageTypes
import archives.tater.endothermic.registry.EndothermicDataAttachments
import archives.tater.endothermic.registry.EndothermicEntities
import archives.tater.endothermic.registry.EndothermicItems
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory


object Endothermic : ModInitializer {
    const val MOD_ID = "endothermic"

    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        EndothermicDamageTypes.init()
        EndothermicDataAttachments.init()
        EndothermicItems.init()
        EndothermicEntities.init()
	}
}