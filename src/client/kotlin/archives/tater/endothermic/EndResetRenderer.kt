package archives.tater.endothermic

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import kotlin.math.log
import kotlin.math.pow

object EndResetRenderer : WorldRenderEvents.AfterEntities, ClientTickEvents.EndWorldTick {
    var progress: Int = -1

    const val MAX_RADIUS = 100.0
    const val LAYERS = 8
    const val HALF_HEIGHT = 1024f
    const val EXPANSION_COEFFICIENT = 1f
    const val EXPANSION_BASE = 1.05f
    const val LAYER_OFFSET = 5
    val MAX_PROGRESS = log(LAYERS * MAX_RADIUS / EXPANSION_COEFFICIENT + 1, EXPANSION_BASE.toDouble()).toInt() - LAYER_OFFSET * (1 - LAYERS)


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

    private fun VertexConsumer.drawLayer(matrix: MatrixStack.Entry, radius: Float, alpha: Int) {
        quad(matrix, -radius, 0f, -radius, -radius, HALF_HEIGHT, radius, alpha)
        quad(matrix, -radius, 0f, radius, radius, HALF_HEIGHT, radius, alpha)
        quad(matrix, radius, 0f, radius, radius, HALF_HEIGHT, -radius, alpha)
        quad(matrix, radius, 0f, -radius, -radius, HALF_HEIGHT, -radius, alpha)

        quad(matrix, -radius, -HALF_HEIGHT, -radius, -radius, 0f, radius, alpha)
        quad(matrix, -radius, -HALF_HEIGHT, radius, radius, 0f, radius, alpha)
        quad(matrix, radius, -HALF_HEIGHT, radius, radius, 0f, -radius, alpha)
        quad(matrix, radius, -HALF_HEIGHT, -radius, -radius, 0f, -radius, alpha)
    }

    override fun afterEntities(context: WorldRenderContext) {
        if (progress == -1) return
        val progress = if (progress >= MAX_PROGRESS) progress.toFloat() else progress + context.tickCounter().getTickProgress(false)
        val consumer = context.consumers()!!.getBuffer(RenderLayer.getLightning())

        val matrices = context.matrixStack()!!
        matrices.push()
        val cameraPos = context.camera().cameraPos
        matrices.translate(0.5 - cameraPos.x, 0.0, 0.5 - cameraPos.z) // Always at (0, cameraY, 0)
        val matrix = matrices.peek()
        consumer.apply {
            repeat (LAYERS) {
                val radius = (EXPANSION_COEFFICIENT * (EXPANSION_BASE.pow(progress + LAYER_OFFSET * (it + 1 - LAYERS)) - 1)) * (it + 1) / LAYERS
//                val opacity = progress - LAYER_OFFSET * (it + 1 - LAYERS)
                if (radius > 0)
                    drawLayer(matrix, radius, 255 / LAYERS)
            }
        }
        matrices.pop()
    }

    override fun onEndTick(world: ClientWorld) {
        if (world.tickManager.shouldTick() && progress >= 0 && progress < MAX_PROGRESS)
            progress++
    }

}