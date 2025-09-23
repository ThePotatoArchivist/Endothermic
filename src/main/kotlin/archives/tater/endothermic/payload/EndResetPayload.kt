package archives.tater.endothermic.payload

import archives.tater.endothermic.Endothermic
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.CustomPayload.Id
import io.netty.buffer.ByteBuf

data object EndResetPayload : CustomPayload {
    val CODEC: PacketCodec<ByteBuf, EndResetPayload> = PacketCodec.unit(this)
    val ID = Id<EndResetPayload>(Endothermic.id("end_reset"))

    override fun getId(): Id<out CustomPayload> = ID
}