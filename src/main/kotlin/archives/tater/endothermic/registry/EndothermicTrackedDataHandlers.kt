package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricTrackedDataRegistry
import net.minecraft.entity.LazyEntityReference
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.math.BlockPos

object EndothermicTrackedDataHandlers {
    private fun <T> register(path: String, codec: PacketCodec<in RegistryByteBuf, T>): TrackedDataHandler<T> {
        val handler = TrackedDataHandler.create(codec)
        FabricTrackedDataRegistry.register(Endothermic.id(path), handler)
        return handler
    }

    val BLOCK_POS_LIST = register<List<BlockPos>>("block_pos_list", BlockPos.PACKET_CODEC.collect(PacketCodecs.toList()))
    val END_CRYSTAL_LIST = register<List<LazyEntityReference<EndCrystalEntity>>>("end_crystal_list", LazyEntityReference.createPacketCodec<EndCrystalEntity>().collect(PacketCodecs.toList()))

    fun init() {

    }
}