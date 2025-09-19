package archives.tater.endothermic.datagen

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.registry.EndothermicItems
import archives.tater.endothermic.util.addDamageType
import archives.tater.endothermic.util.addEnchantment
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
        translationBuilder.add(Endothermic.END_CRYSTALS_BOSSBAR, "End Crystals")
        translationBuilder.addEnchantment(EnchantmentGenerator.DASH, "Dash", "At high glide speeds, doubles wall & fall damage, negates all other types of damage, and treats melee attacks as projectile attacks")
    }
}