package archives.tater.endothermic.client.particle

import archives.tater.endothermic.util.SimpleSingleSpriteParticleFactory
import net.minecraft.client.particle.ParticleTextureSheet
import net.minecraft.client.particle.SpriteBillboardParticle
import net.minecraft.client.render.Camera
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3f

class MovementParticle(clientWorld: ClientWorld, x: Double, y: Double, z: Double, vx: Double, vy: Double, vz: Double)
    : SpriteBillboardParticle(clientWorld, x, y, z) {

    private val direction = Vector3f(vx.toFloat(), vy.toFloat(), vz.toFloat()).normalize()

    init {
        scale = 0.25f
        maxAge = 5
        gravityStrength = 0.0f
    }

    override fun getBrightness(tint: Float): Int {
        return LightmapTextureManager.pack(15, LightmapTextureManager.getSkyLightCoordinates(super.getBrightness(tint)))
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickProgress: Float) {
        alpha = (1 - (age + tickProgress) / maxAge.toFloat()).coerceAtLeast(0f)

        val quaternionf = Quaternionf()
        quaternionf.rotateTo(Vector3f(1f, 0f, 0f), direction)
        render(vertexConsumer, camera, quaternionf, tickProgress)
        quaternionf.rotateY(MathHelper.PI)
        render(vertexConsumer, camera, quaternionf, tickProgress)
    }

    override fun getType(): ParticleTextureSheet = ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT

    companion object Factory : SimpleSingleSpriteParticleFactory(::MovementParticle)
}