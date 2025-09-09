package archives.tater.endothermic.datagen

import archives.tater.endothermic.registry.EndothermicItems
import archives.tater.endothermic.util.addDamageType
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class LangGenerator(dataOutput: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) :
    FabricLanguageProvider(dataOutput, registryLookup) {
    override fun generateTranslations(
        registryLookup: RegistryWrapper.WrapperLookup?,
        translationBuilder: TranslationBuilder
    ) {
        translationBuilder.add(EndothermicItems.INDESTRUCTIBLE, "Indestructible")
        translationBuilder.addDamageType(DamageTypeGenerator.DASH_MESSAGE_ID, "%s was splattered by %s", itemMessage = "%s was splattered by %s using %s")
    }
}