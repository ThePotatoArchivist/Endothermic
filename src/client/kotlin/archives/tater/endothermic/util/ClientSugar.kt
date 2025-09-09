package archives.tater.endothermic.util

import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters
import net.minecraft.util.Identifier

inline fun MultiPhaseParameters(affectsOutline: Boolean = false, init: MultiPhaseParameters.Builder.() -> Unit): MultiPhaseParameters =
    MultiPhaseParameters.builder().apply(init).build(affectsOutline)

inline fun RenderPipeline(vararg snippets: RenderPipeline.Snippet, init: RenderPipeline.Builder.() -> Unit): RenderPipeline =
    RenderPipeline.builder(*snippets).apply(init).build()

/**
 * Sets the fragment and vertex shader to the same Identifier
 */
fun RenderPipeline.Builder.withShaders(shader: Identifier) {
    withFragmentShader(shader)
    withVertexShader(shader)
}