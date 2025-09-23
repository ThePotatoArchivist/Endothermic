package archives.tater.endothermic.datagen

import net.minecraft.data.DataOutput
import net.minecraft.data.tag.SimpleTagProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.world.biome.Biome
import java.util.concurrent.CompletableFuture

class BiomeTagGenerator(
    dataOutput: DataOutput,
    completableFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : SimpleTagProvider<Biome>(dataOutput, RegistryKeys.BIOME, completableFuture) {
    override fun configure(registries: RegistryWrapper.WrapperLookup) {
    }
}