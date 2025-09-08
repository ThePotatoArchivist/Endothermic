package archives.tater.endothermic

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.abs

object EndResetRenderer : WorldRenderEvents.AfterEntities, ClientTickEvents.EndWorldTick, ClientWorldEvents.AfterClientWorldChange, HudElement {
    var progress: Int = -1
        private set

    const val MAX_RADIUS = 16f
    const val LAYERS = 8
    const val HALF_HEIGHT = 2048f
    const val LAYER_OFFSET = 4
    const val MAX_PROGRESS = 250
    const val OPACITY_DURATION = 16

    const val OVERLAY_DURATION = 50

    val HUD_ID = Endothermic.id("end_reset_overlay")

    fun reset() {
        progress = -1
    }

    fun start() {
        progress = 0
    }

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
        val progress = if (progress >= MAX_PROGRESS) progress.toFloat() else progress + context.tickCounter()
            .getTickProgress(false)
        val consumer = context.consumers()!!.getBuffer(EndothermicRenderLayers.FOUNTAIN)

        val matrices = context.matrixStack()!!
        matrices.push()
        val cameraPos = context.camera().cameraPos
        matrices.translate(Vec3d(0.5 - cameraPos.x, 0.0, 0.5 - cameraPos.z)) // Always at (0, cameraY, 0)
        val matrix = matrices.peek()
        val outerLayer = progress.toInt() / LAYER_OFFSET
        var overlay = 0
        with (consumer) {
            for (i in outerLayer - LAYERS - 1..outerLayer) {
                val radius = (progress * MAX_RADIUS / MAX_PROGRESS + 8f) * (i + 1) / LAYERS
                val opacity =
                    (((progress - LAYER_OFFSET * (i + 1)) / OPACITY_DURATION).coerceIn(0f, 1f) * 255 / (LAYERS - 2)).toInt()
                if (radius > 0 && opacity > 0) {
                    if (radius > abs(cameraPos.x) && radius > abs(cameraPos.z)) {
                        overlay += opacity
                    } else
                        drawLayer(matrix, radius, opacity)
                }
            }
        }
        matrices.pop()

        matrices.push()
        matrices.multiply(context.camera().rotation)
        if (overlay > 0) {
            consumer.quad(matrices.peek(), -1f, -1f, -0.1f, 1f, 1f, -0.1f, overlay.coerceAtMost(255))
        }
        matrices.pop()
    }

    /**
     * For [HudElement]
     */
    override fun render(
        context: DrawContext,
        tickCounter: RenderTickCounter
    ) {
        val opacity = 255 * (progress - (MAX_PROGRESS - OVERLAY_DURATION)) / OVERLAY_DURATION
        if (opacity <= 0) return
        context.fill(0, 0, context.scaledWindowWidth, context.scaledWindowHeight, ColorHelper.getArgb(opacity, 255, 255, 255))
    }

    override fun onEndTick(world: ClientWorld) {
        if (world.tickManager.shouldTick() && progress >= 0 && progress < MAX_PROGRESS)
            progress++
    }

    override fun afterWorldChange(client: MinecraftClient?, world: ClientWorld?) = reset()
}