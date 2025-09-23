package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.worldgen.AbsoluteStructurePlacement
import archives.tater.endothermic.worldgen.GridStructurePlacement
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.world.gen.chunk.placement.StructurePlacementType

object EndothermicWorldgen {
    private fun <T> tagOf(registryRef: RegistryKey<Registry<T>>, path: String): TagKey<T> = TagKey.of(registryRef, Endothermic.id(path))

    @JvmField
    val ABSOLUTE_STRUCTURE_PLACEMENT: StructurePlacementType<AbsoluteStructurePlacement> = Registry.register(
        Registries.STRUCTURE_PLACEMENT,
        Endothermic.id("absolute"),
        StructurePlacementType(AbsoluteStructurePlacement::CODEC)
    )

    @JvmField
    val GRID_STRUCTURE_PLACEMENT: StructurePlacementType<GridStructurePlacement> = Registry.register(
        Registries.STRUCTURE_PLACEMENT,
        Endothermic.id("grid"),
        StructurePlacementType(GridStructurePlacement::CODEC)
    )

    @JvmField
    val CENTER_FUNNELING = tagOf(RegistryKeys.TEMPLATE_POOL, "center_funneling")

    fun init() {

    }
}