package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.particle.ParticleType
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object EndothermicParticles {
    private fun <T: ParticleType<*>> register(id: Identifier, type: T): T =
        Registry.register(Registries.PARTICLE_TYPE, id, type)

    private fun <T: ParticleType<*>> register(path: String, type: T) = register(Endothermic.id(path), type)

    private fun register(path: String, alwaysShow: Boolean = false): SimpleParticleType = register(path, FabricParticleTypes.simple(alwaysShow))

    @JvmField
    val DASH = register("dash")

    fun init() {

    }
}