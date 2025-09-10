package archives.tater.endothermic.util

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory
import net.minecraft.client.particle.ParticleFactory
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.SimpleParticleType

open class SingleSpriteParticleFactory<T: ParticleEffect>(private val factory: (T, ClientWorld, Double, Double, Double, Double, Double, Double) -> SpriteBillboardParticle) : PendingParticleFactory<T> {
    override fun create(provider: FabricSpriteProvider): ParticleFactory<T> = ParticleFactory { parameters, world, x, y, z, vx, vy, vz ->
        factory(parameters, world, x, y, z, vx, vy, vz).apply {
            setSprite(provider)
        }
    }
}

open class SimpleSingleSpriteParticleFactory(private val factory: (ClientWorld, Double, Double, Double, Double, Double, Double) -> SpriteBillboardParticle)
    : SingleSpriteParticleFactory<SimpleParticleType>({ _, world, x, y, z, vx, vy, vz -> factory(world, x, y, z, vx, vy, vz) })
