package archives.tater.endothermic

import archives.tater.endothermic.client.particle.registerParticleFactories
import archives.tater.endothermic.client.render.EndothermicRenderLayers
import archives.tater.endothermic.client.render.entity.registerEndothermicEntityRenderers
import archives.tater.endothermic.client.render.environment.CentralEndIslandSparkleRenderer
import archives.tater.endothermic.client.render.environment.EndResetRenderer
import archives.tater.endothermic.payload.EndResetPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements

object EndothermicClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EndothermicRenderLayers.init()
        registerParticleFactories()
        registerEndothermicEntityRenderers()

        WorldRenderEvents.AFTER_ENTITIES.register(CentralEndIslandSparkleRenderer)
        ClientTickEvents.END_WORLD_TICK.register(CentralEndIslandSparkleRenderer)
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(CentralEndIslandSparkleRenderer)

		WorldRenderEvents.AFTER_ENTITIES.register(EndResetRenderer)
		ClientTickEvents.END_WORLD_TICK.register(EndResetRenderer)
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(EndResetRenderer)
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, EndResetRenderer.HUD_ID,
            EndResetRenderer
        )

        ClientPlayNetworking.registerGlobalReceiver(EndResetPayload.ID) { _, _ ->
            EndResetRenderer.start()
        }
	}
}