package archives.tater.endothermic

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.item.Items
import net.minecraft.util.ActionResult

object EndothermicClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EndothermicRenderLayers.init()

        WorldRenderEvents.AFTER_ENTITIES.register(CentralEndIslandSparkleRenderer)

		WorldRenderEvents.AFTER_ENTITIES.register(EndResetRenderer)
		ClientTickEvents.END_WORLD_TICK.register(EndResetRenderer)
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(EndResetRenderer)
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, EndResetRenderer.HUD_ID, EndResetRenderer)

		UseItemCallback.EVENT.register { player, world, hand ->
			if (world.isClient && player.getStackInHand(hand).isOf(Items.NETHER_STAR)) {
				EndResetRenderer.start()
				ActionResult.SUCCESS
			} else ActionResult.PASS
		}
	}
}