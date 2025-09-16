package archives.tater.endothermic.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.data.tag.SimpleTagProvider
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.EnchantmentTags
import java.util.concurrent.CompletableFuture

class EnchantmentTagGenerator(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : SimpleTagProvider<Enchantment>(output, RegistryKeys.ENCHANTMENT, registriesFuture) {

    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        with (builder(EnchantmentTags.TREASURE)) {
            add(EnchantmentGenerator.DASH)
        }
    }

}