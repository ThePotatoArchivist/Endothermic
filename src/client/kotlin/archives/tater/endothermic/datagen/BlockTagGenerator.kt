package archives.tater.endothermic.datagen

import archives.tater.endothermic.registry.EndothermicBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.BlockTags
import java.util.concurrent.CompletableFuture

class BlockTagGenerator(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.BlockTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        with (valueLookupBuilder(EndothermicBlocks.VELOCITITE_FIELD_BORDER)) {
            add(EndothermicBlocks.VELOCITITE)
        }
        with (valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)) {
            add(EndothermicBlocks.VELOCITITE)
        }
        with (valueLookupBuilder(BlockTags.NEEDS_DIAMOND_TOOL)) {
            add(EndothermicBlocks.VELOCITITE)
        }
        with (valueLookupBuilder(BlockTags.DRAGON_IMMUNE)) {
            add(EndothermicBlocks.VELOCITITE_FIELD)
        }
    }
}