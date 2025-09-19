package archives.tater.endothermic.util

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Util.createTranslationKey

fun FabricLanguageProvider.TranslationBuilder.addDamageType(msgId: String, message: String? = null, playerMessage: String? = null, itemMessage: String? = null) {
    message?.let { add("death.attack.$msgId", it) }
    playerMessage?.let { add("death.attack.$msgId.player", it) }
    itemMessage?.let { add("death.attack.$msgId.item", it) }
}

fun FabricLanguageProvider.TranslationBuilder.addEnchantment(enchantment: RegistryKey<Enchantment>, name: String, description: String) {
    addEnchantment(enchantment, name)
    add(createTranslationKey("enchantment", enchantment.value) + ".desc", description);
}