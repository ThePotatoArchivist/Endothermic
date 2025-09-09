package archives.tater.endothermic.client.render.environment

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.client.render.EndothermicRenderLayers
import archives.tater.endothermic.util.invoke
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.MathHelper.TAU
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.sin

object CentralEndIslandSparkleRenderer : WorldRenderEvents.AfterEntities, ClientTickEvents.EndWorldTick, ClientWorldEvents.AfterClientWorldChange {
    val SPARKLE_POS = Vec3d(0.0, 60.0, 0.0)
    const val MAX_RADIUS = 3f / 32
    const val ROTATION_DURATION = 30 * 20
    const val ROTATION_SPEED = TAU / ROTATION_DURATION

    const val ISLAND_RADIUS = 3 * 16 // Approximation
    const val FADE_OUT_DISTANCE = 3 * 16

    val PRIMARY_TEXTURE = Endothermic.id("textures/environment/sparkle_primary.png")
    val SECONDARY_TEXTURE = Endothermic.id("textures/environment/sparkle_secondary.png")

    private var currentTime = 0

    private fun VertexConsumer.vertex(matrix: MatrixStack.Entry, x: Float, y: Float, z: Float, alpha: Int, u: Float, v: Float) {
        vertex(matrix, x, y, z)
        color(255, 255, 255, alpha)
        texture(u, v)
    }

    private fun VertexConsumer.quad(matrix: MatrixStack.Entry, x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float, alpha: Int) {
        vertex(matrix, x1, y1, z1, alpha, 0f, 0f)
        vertex(matrix, x2, y1, z2, alpha, 1f, 0f)
        vertex(matrix, x2, y2, z2, alpha, 1f, 1f)
        vertex(matrix, x1, y2, z1, alpha, 0f, 1f)
    }

    private fun shouldRender(world: World) = world.registryKey == World.END

    override fun afterEntities(context: WorldRenderContext) {
        if (!shouldRender(context.world())) return

        val radius = MAX_RADIUS * ((context.camera().cameraPos.subtract(0.0, 0.0, 0.0).horizontalLength() - context.worldRenderer().viewDistance * 16 - ISLAND_RADIUS) / FADE_OUT_DISTANCE).coerceAtMost(1.0).toFloat()
        if (radius < 0) return

        val matrices = context.matrixStack()!!
        val progress = currentTime + context.tickCounter().getTickProgress(false)
        val consumers = context.consumers()!!
        val scale = context.camera().pos.distanceTo(SPARKLE_POS).toFloat()

        matrices.push()
        matrices.translate(context.camera().pos.multiply(-1.0).add(SPARKLE_POS)) // Always render relative to world
        matrices.multiply(context.camera().rotation)

        matrices.push()
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(ROTATION_SPEED * progress))
        matrices.scale(scale, scale, scale)
        consumers.getBuffer(EndothermicRenderLayers.SPARKLE(PRIMARY_TEXTURE)).quad(matrices.peek(), -radius, -radius, 0f, radius, radius, 0f, 63 + (128 * (0.5f * sin(0.0625f * progress) + 0.5)).toInt())
        matrices.pop()

        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(-1.5f * ROTATION_SPEED * progress))
        matrices.translate(0f, 0f, 1f)
        matrices.scale(scale, scale, scale)
        consumers.getBuffer(EndothermicRenderLayers.SPARKLE(SECONDARY_TEXTURE)).quad(matrices.peek(), -radius, -radius, 0f, radius, radius, 0f, 31 + (63 * (-0.5f * sin(0.0625f * progress) + 0.5)).toInt())

        matrices.pop()
    }

    override fun onEndTick(world: ClientWorld) {
        if (!shouldRender(world)) return
        currentTime++
    }

    override fun afterWorldChange(
        client: MinecraftClient?,
        world: ClientWorld?
    ) {
        currentTime = 0
    }
}