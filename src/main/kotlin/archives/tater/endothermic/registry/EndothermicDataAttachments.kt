package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.Identifier

@Suppress("UnstableApiUsage")
object EndothermicDataAttachments {
    private fun <T: Any> create(id: Identifier, init: AttachmentRegistry.Builder<T>.() -> Unit = {}): AttachmentType<T> =
        AttachmentRegistry.create(id) { init(it) }

    private fun <T: Any> create(path: String, init: AttachmentRegistry.Builder<T>.() -> Unit = {}): AttachmentType<T> =
        create(Endothermic.id(path), init)

    @JvmField
    val DASH_TICKS: AttachmentType<Int> = create("dash_ticks") {
        syncWith(PacketCodecs.INTEGER, AttachmentSyncPredicate.all())
        persistent(Codec.INT)
    }

    fun init() {

    }
}