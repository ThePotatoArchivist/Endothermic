package archives.tater.endothermic.datagen

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.registry.EndothermicParticles
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.data.DataOutput.OutputType
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class ParticleGenerator(
    dataOutput: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>,
) : FabricCodecDataProvider<List<Identifier>>(dataOutput, registriesFuture, OutputType.RESOURCE_PACK, "particles", CODEC) {

    override fun configure(
        provider: BiConsumer<Identifier, List<Identifier>>,
        lookup: RegistryWrapper.WrapperLookup?
    ) {
        fun register(type: ParticleType<*>, textures: List<Identifier>) {
            provider.accept(Registries.PARTICLE_TYPE.getId(type)!!, textures)
        }

        register(EndothermicParticles.DASH, listOf(Endothermic.id("dash")))
    }

    override fun getName(): String = "Particle Texture Data"

    companion object {
        val CODEC: Codec<List<Identifier>> = Identifier.CODEC.listOf(1, Int.MAX_VALUE).fieldOf("textures").codec()
    }
}