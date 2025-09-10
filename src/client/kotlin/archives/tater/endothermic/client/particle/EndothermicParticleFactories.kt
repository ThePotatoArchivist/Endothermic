package archives.tater.endothermic.client.particle

import archives.tater.endothermic.registry.EndothermicParticles
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry

internal fun registerParticleFactories() {
    with(ParticleFactoryRegistry.getInstance()) {
        register(EndothermicParticles.DASH, MovementParticle)
    }
}
