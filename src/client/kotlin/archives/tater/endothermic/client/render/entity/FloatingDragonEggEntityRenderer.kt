package archives.tater.endothermic.client.render.entity

import archives.tater.endothermic.entity.FloatingDragonEggEntity
import archives.tater.endothermic.util.minus
import archives.tater.endothermic.util.resolve
import net.minecraft.block.Blocks
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.entity.EndCrystalEntityRenderer
import net.minecraft.client.render.entity.EnderDragonEntityRenderer
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.state.EntityRenderState
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d

class FloatingDragonEggEntityRenderer(context: EntityRendererFactory.Context) : EntityRenderer<FloatingDragonEggEntity, FloatingDragonEggEntityRenderer.State>(context) {
    private val blockRenderer: BlockRenderManager = context.blockRenderManager

    override fun createRenderState(): State = State()

    override fun updateRenderState(entity: FloatingDragonEggEntity, state: State, tickProgress: Float) {
        super.updateRenderState(entity, state, tickProgress)

        val anchorPos = entity.pos.add(0.0, Y_OFFSET, 0.0)

        state.crystalPositions = entity.crystals.mapNotNull {
            val endCrystal = it.resolve(entity.world) ?: return@mapNotNull null

            val crystalPos = endCrystal
                .getLerpedPos(tickProgress)
                .add(0.0, EndCrystalEntityRenderer.getYOffset(endCrystal.endCrystalAge + tickProgress).toDouble(), 0.0)

            crystalPos - anchorPos
        }
    }

    override fun render(
        state: State,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()
        matrices.translate(-0.5f, 0f, -0.5f)
        blockRenderer.renderBlockAsEntity(Blocks.DRAGON_EGG.defaultState, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV)
        matrices.pop()

        matrices.push()
        matrices.translate(0.0, Y_OFFSET, 0.0)
        for (pos in state.crystalPositions)
            EnderDragonEntityRenderer.renderCrystalBeam(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat(), state.age, matrices, vertexConsumers, light)
        matrices.pop()

        super.render(state, matrices, vertexConsumers, light)
    }

    override fun shouldRender(
        entity: FloatingDragonEggEntity?,
        frustum: Frustum?,
        x: Double,
        y: Double,
        z: Double
    ): Boolean = true

    companion object {
        const val Y_OFFSET = -1.5
    }

    class State : EntityRenderState() {
        @JvmField
        var crystalPositions: List<Vec3d> = listOf()
    }
}