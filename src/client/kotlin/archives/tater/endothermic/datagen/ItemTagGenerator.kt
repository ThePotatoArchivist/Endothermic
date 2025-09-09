package archives.tater.endothermic.datagen

import archives.tater.endothermic.registry.EndothermicItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Items
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ItemTagGenerator(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.ItemTagProvider(output, registriesFuture) {

    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        valueLookupBuilder(EndothermicItems.INDESTRUCTIBLE).apply {
            add(Items.DRAGON_EGG)
        }
    }

}