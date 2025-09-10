package archives.tater.endothermic

import archives.tater.endothermic.datagen.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryKeys

object EndothermicDataGenerator : DataGeneratorEntrypoint {
    override fun buildRegistry(registryBuilder: RegistryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, DamageTypeGenerator)
    }

	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        fabricDataGenerator.createPack().apply {
            addProvider(::ItemTagGenerator)
            addProvider(::DamageTypeGenerator)
            addProvider(::DamageTypeTagGenerator)

            addProvider(::ModelGenerator)
            addProvider(::LangGenerator)
            addProvider(::ParticleGenerator)
        }
	}
}