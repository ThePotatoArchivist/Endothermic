package archives.tater.endothermic.datagen

import archives.tater.endothermic.registry.EndothermicEntities
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class EntityTagGenerator(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricTagProvider.EntityTypeTagProvider(output, registriesFuture) {

    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        valueLookupBuilder(EndothermicEntities.END_NATIVE).add(
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.ENDER_DRAGON,
            EntityType.SHULKER,
        )
    }
}