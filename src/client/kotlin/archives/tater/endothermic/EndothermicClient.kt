package archives.tater.endothermic

import archives.tater.endothermic.client.particle.registerParticleFactories
import archives.tater.endothermic.client.render.EndothermicRenderLayers
import archives.tater.endothermic.client.render.entity.registerEndothermicEntityRenderers
import archives.tater.endothermic.client.render.environment.CentralEndIslandSparkleRenderer
import archives.tater.endothermic.client.render.environment.EndResetRenderer
import archives.tater.endothermic.payload.EggTeleportPayload
import archives.tater.endothermic.payload.EndResetPayload
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.MathHelper.lerp

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
        ClientPlayNetworking.registerGlobalReceiver(EggTeleportPayload.ID) { payload, context ->
            val world = context.player().world
            val random = world.random
            val pos = payload.source
            val blockPos = payload.destination

            repeat(128) {
                val delta = random.nextDouble()
                world.addParticleClient(
                    ParticleTypes.PORTAL,
                    lerp(delta, blockPos.x.toDouble(), pos.x.toDouble()) + (random.nextDouble() - 0.5) + 0.5,
                    lerp(delta, blockPos.y.toDouble(), pos.y.toDouble()) + random.nextDouble() - 0.5,
                    lerp(delta, blockPos.z.toDouble(), pos.z.toDouble()) + (random.nextDouble() - 0.5) + 0.5,
                    ((random.nextFloat() - 0.5f) * 0.2f).toDouble(),
                    ((random.nextFloat() - 0.5f) * 0.2f).toDouble(), ((random.nextFloat() - 0.5f) * 0.2f).toDouble()
                )
            }
        }
	}
}