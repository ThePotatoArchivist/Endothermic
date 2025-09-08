package archives.tater.endothermic

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d

object CentralEndIslandSparkleRenderer : WorldRenderEvents.AfterEntities {
    val SPARKLE_POS = Vec3d(0.0, 90.0, 0.0)
    const val MAX_RADIUS = 1f / 32

    private fun VertexConsumer.vertex(matrix: MatrixStack.Entry, x: Float, y: Float, z: Float, alpha: Int) {
        vertex(matrix, x, y, z)
        color(255, 255, 255, alpha)
    }

    private fun VertexConsumer.quad(matrix: MatrixStack.Entry, x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float, alpha: Int) {
        vertex(matrix, x1, y1, z1, alpha)
        vertex(matrix, x2, y1, z2, alpha)
        vertex(matrix, x2, y2, z2, alpha)
        vertex(matrix, x1, y2, z1, alpha)
    }

    override fun afterEntities(context: WorldRenderContext) {
        val radius = MAX_RADIUS * ((context.camera().cameraPos.subtract(0.0, 0.0, 0.0).horizontalLength() - (context.worldRenderer().viewDistance - 1) * 16) / 16).coerceAtMost(1.0).toFloat()
        if (radius < 0) return;

        val matrices = context.matrixStack()!!

        matrices.push()
        matrices.translate(context.camera().pos.multiply(-1.0).add(SPARKLE_POS)) // Always render relative to world
        matrices.multiply(context.camera().rotation)
        val scale = context.camera().pos.distanceTo(SPARKLE_POS).toFloat()
        matrices.scale(scale, scale, scale)

        context.consumers()!!.getBuffer(EndothermicRenderLayers.FOUNTAIN).apply {
            quad(matrices.peek(), -radius, -radius, 0f, radius, radius, 0f, 255)
        }

        matrices.pop()
    }
}