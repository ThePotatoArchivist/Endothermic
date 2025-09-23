package archives.tater.endothermic.worldgen

import archives.tater.endothermic.registry.EndothermicWorldgen
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.gen.chunk.placement.StructurePlacement
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator
import net.minecraft.world.gen.chunk.placement.StructurePlacementType
import java.util.*

class AbsoluteStructurePlacement(
    locateOffset: Vec3i,
    frequencyReductionMethod: FrequencyReductionMethod,
    frequency: Float,
    salt: Int,
    @Suppress("DEPRECATION") exclusionZone: Optional<ExclusionZone>,
    val chunks: Set<ChunkPos>,
) : StructurePlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone) {
    override fun isStartChunk(
        calculator: StructurePlacementCalculator?,
        chunkX: Int,
        chunkZ: Int
    ): Boolean = chunks.contains(ChunkPos(chunkX, chunkZ))

    override fun getType(): StructurePlacementType<AbsoluteStructurePlacement> = EndothermicWorldgen.ABSOLUTE_STRUCTURE_PLACEMENT

    companion object {
        val CODEC: MapCodec<AbsoluteStructurePlacement> = RecordCodecBuilder.mapCodec {
            buildCodec(it).and(
                ChunkPos.CODEC.listOf().xmap(List<ChunkPos>::toSet, Set<ChunkPos>::toList).fieldOf("chunks").forGetter(AbsoluteStructurePlacement::chunks)
            ).apply(it, ::AbsoluteStructurePlacement)
        }
    }
}