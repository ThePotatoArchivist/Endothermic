package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.Identifier
import net.minecraft.util.Unit as MCUnit

@Suppress("UnstableApiUsage")
object EndothermicDataAttachments {
    private inline fun <T: Any> create(id: Identifier, crossinline init: AttachmentRegistry.Builder<T>.() -> Unit = {}): AttachmentType<T> =
        AttachmentRegistry.create(id) { init(it) }

    private inline fun <T: Any> create(path: String, crossinline init: AttachmentRegistry.Builder<T>.() -> Unit = {}): AttachmentType<T> =
        create(Endothermic.id(path), init)

    // Used by: LivingEntity
    @JvmField
    val DASH_TICKS: AttachmentType<Int> = create("dash_ticks") {
        syncWith(PacketCodecs.INTEGER, AttachmentSyncPredicate.all())
        persistent(Codec.INT)
    }

    // Used by: World
    @JvmField
    val GATEWAYS_OPEN: AttachmentType<MCUnit> = create("gateway_open") {
        syncWith(MCUnit.PACKET_CODEC, AttachmentSyncPredicate.all())
        persistent(MCUnit.CODEC)
    }

    fun init() {

    }
}