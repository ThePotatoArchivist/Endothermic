package archives.tater.endothermic.worldgen

import archives.tater.endothermic.registry.EndothermicWorldgen
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.math.Vec3i
import net.minecraft.world.gen.chunk.placement.StructurePlacement
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator
import net.minecraft.world.gen.chunk.placement.StructurePlacementType
import java.util.*
import kotlin.math.abs

class GridStructurePlacement(
    locateOffset: Vec3i,
    frequencyReductionMethod: FrequencyReductionMethod,
    frequency: Float,
    salt: Int,
    @Suppress("DEPRECATION") exclusionZone: Optional<ExclusionZone>,
    val gridSpacing: Int,
    val holeRadius: Int,
) : StructurePlacement(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone) {
    override fun isStartChunk(
        calculator: StructurePlacementCalculator?,
        chunkX: Int,
        chunkZ: Int
    ): Boolean = chunkX % gridSpacing == 0 && chunkZ % gridSpacing == 0 && (abs(chunkX) > holeRadius || abs(chunkZ) > holeRadius)

    override fun getType(): StructurePlacementType<GridStructurePlacement> = EndothermicWorldgen.GRID_STRUCTURE_PLACEMENT

    companion object {
        val CODEC: MapCodec<GridStructurePlacement> = RecordCodecBuilder.mapCodec {
            buildCodec(it).and(it.group(
                Codec.intRange(1, Integer.MAX_VALUE).fieldOf("grid_spacing").forGetter(GridStructurePlacement::gridSpacing),
                Codec.intRange(0, Integer.MAX_VALUE).fieldOf("hole_radius").forGetter(GridStructurePlacement::holeRadius),
            )).apply(it, ::GridStructurePlacement)
        }
    }
}
