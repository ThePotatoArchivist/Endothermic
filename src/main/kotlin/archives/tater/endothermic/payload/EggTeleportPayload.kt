package archives.tater.endothermic.payload

import archives.tater.endothermic.Endothermic
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

@JvmRecord
data class EggTeleportPayload(val source: BlockPos, val destination: BlockPos) : CustomPayload {
    override fun getId(): CustomPayload.Id<EggTeleportPayload> = ID

    companion object {
        val CODEC: PacketCodec<ByteBuf, EggTeleportPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, EggTeleportPayload::source,
            BlockPos.PACKET_CODEC, EggTeleportPayload::destination,
            ::EggTeleportPayload
        )

        val ID = CustomPayload.Id<EggTeleportPayload>(Endothermic.id("egg_teleport"))
    }
}