package archives.tater.endothermic.datagen

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.client.data.BlockStateModelGenerator
import net.minecraft.client.data.ItemModelGenerator

class ModelGenerator(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {

    }
}