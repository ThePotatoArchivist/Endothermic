package archives.tater.endothermic.datagen

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.block.VelocititeFieldBlock
import archives.tater.endothermic.registry.EndothermicBlocks
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.client.data.*
import net.minecraft.client.data.BlockStateModelGenerator.createAxisRotatedVariantMap
import java.util.*

class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(
            VariantsBlockModelDefinitionCreator.of(EndothermicBlocks.VELOCITITE_FIELD)
                .with(BlockStateVariantMap.models(VelocititeFieldBlock.ON_COOLDOWN).generate { onCooldown ->
                    val suffix = if (onCooldown) "_active" else ""
                    BlockStateModelGenerator.createWeightedVariant(GLIDE_BOOSTER_MODEL.upload(
                        EndothermicBlocks.VELOCITITE_FIELD,
                        suffix,
                        TextureMap.all(TextureMap.getSubId(EndothermicBlocks.VELOCITITE_FIELD, suffix)),
                        blockStateModelGenerator.modelCollector
                    ))
                })
                .coordinate(createAxisRotatedVariantMap())
        )
        blockStateModelGenerator.registerSimpleCubeAll(EndothermicBlocks.VELOCITITE)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }

    companion object {
        val GLIDE_BOOSTER_MODEL = Model(Optional.of(Endothermic.id("block/template_glide_booster")), Optional.empty(), TextureKey.ALL)
    }
}