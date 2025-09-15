package archives.tater.endothermic

import archives.tater.endothermic.mixin.portals.EndGatewayBlockEntityInvoker
import archives.tater.endothermic.util.PairListCodec
import archives.tater.endothermic.util.component1
import archives.tater.endothermic.util.component2
import com.mojang.serialization.Codec
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.PersistentState
import net.minecraft.world.PersistentStateType
import com.mojang.datafixers.util.Pair as DFPair

class EndPortalCache(destinations: Map<ChunkPos, BlockPos> = mapOf()) : PersistentState() {
    private val destinations = destinations.toMutableMap()

    fun getOrCreate(world: ServerWorld, origin: ChunkPos) = destinations.getOrPut(origin) {
        markDirty()
        EndGatewayBlockEntityInvoker.invokeSetupExitPortalLocation(world, origin.startPos)
    }

    fun getOrCreate(world: ServerWorld, origin: BlockPos) = getOrCreate(world, ChunkPos(origin))

    companion object {
        val CODEC: Codec<EndPortalCache> = PairListCodec(ChunkPos.CODEC, BlockPos.CODEC)
            .listOf()
            .xmap(
                { EndPortalCache(it.associate { (key, value) -> key to value }) },
                { it.destinations.map { (key, value) -> DFPair(key, value) } }
            )

        @JvmField
        val TYPE = PersistentStateType<EndPortalCache>("${Endothermic.MOD_ID}_portal_cache", ::EndPortalCache, CODEC, null)
    }
}