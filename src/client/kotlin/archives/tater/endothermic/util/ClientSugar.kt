package archives.tater.endothermic.util

import com.mojang.blaze3d.pipeline.RenderPipeline
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters

inline fun MultiPhaseParameters(affectsOutline: Boolean = false, init: MultiPhaseParameters.Builder.() -> Unit): MultiPhaseParameters =
    MultiPhaseParameters.builder().apply(init).build(affectsOutline)

inline fun RenderPipeline(vararg snippets: RenderPipeline.Snippet, init: RenderPipeline.Builder.() -> Unit): RenderPipeline =
    RenderPipeline.builder(*snippets).apply(init).build()