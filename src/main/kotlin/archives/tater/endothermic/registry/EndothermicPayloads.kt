package archives.tater.endothermic.registry

import archives.tater.endothermic.payload.EggTeleportPayload
import archives.tater.endothermic.payload.EndResetPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

internal fun registerPayloads() {
    PayloadTypeRegistry.playS2C().apply {
        register(EndResetPayload.ID, EndResetPayload.CODEC)
        register(EggTeleportPayload.ID, EggTeleportPayload.CODEC)
    }
}
