package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.util.Identifier
import net.minecraft.util.Unit as MCUnit

@Suppress("UnstableApiUsage")
object EndothermicDataAttachments {
    private inline fun <T: Any> create(id: Identifier, crossinline init: AttachmentRegistry.Builder<T>.() -> Unit = {}): AttachmentType<T> =
        AttachmentRegistry.create(id) { init(it) }

    private inline fun <T: Any> create(path: String, crossinline init: AttachmentRegistry.Builder<T>.() -> Unit = {}): AttachmentType<T> =
        create(Endothermic.id(path), init)

    // Used by: World
    @JvmField
    val GATEWAYS_OPEN: AttachmentType<MCUnit> = create("gateway_open") {
        syncWith(MCUnit.PACKET_CODEC, AttachmentSyncPredicate.all())
        persistent(MCUnit.CODEC)
    }

    fun init() {

    }
}