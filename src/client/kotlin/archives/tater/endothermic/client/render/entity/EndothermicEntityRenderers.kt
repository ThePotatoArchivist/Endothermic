package archives.tater.endothermic.client.render.entity

import archives.tater.endothermic.registry.EndothermicEntities
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

internal fun registerEndothermicEntityRenderers() {
    EntityRendererRegistry.register(EndothermicEntities.FLOATING_DRAGON_EGG, ::FloatingDragonEggEntityRenderer)
}
