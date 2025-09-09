package archives.tater.endothermic.datagen

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.registry.EndothermicDamageTypes
import archives.tater.endothermic.util.add
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class DamageTypeGenerator(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricDynamicRegistryProvider(output, registriesFuture) {

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
        context(registries) {
            entries.add(EndothermicDamageTypes.DASH_ATTACK)
        }
    }

    override fun getName(): String = "Damage Types"

    companion object : RegistryBuilder.BootstrapFunction<DamageType> {
        const val DASH_MESSAGE_ID = "${Endothermic.MOD_ID}.dash"

        override fun run(registerable: Registerable<DamageType>) {
            registerable.register(EndothermicDamageTypes.DASH_ATTACK, DamageType(DASH_MESSAGE_ID, 0.1f))
        }
    }
}