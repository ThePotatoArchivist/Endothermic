package archives.tater.endothermic

import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DestFactor
import com.mojang.blaze3d.platform.SourceFactor
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexFormats

object EndothermicRenderLayers {
    val RENDERTYPE_FOUNTAIN: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_SNIPPET).apply {
            withLocation(Endothermic.id("pipeline/fountain"))
            withVertexShader(Endothermic.id("core/rendertype_fountain"))
            withFragmentShader(Endothermic.id("core/rendertype_fountain"))
            withBlend(BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE))
            withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
        }.build()
    )

    val FOUNTAIN: RenderLayer = RenderLayer.of("${Endothermic.MOD_ID}_fountain", 1536, false, true, RENDERTYPE_FOUNTAIN,
        RenderLayer.MultiPhaseParameters.builder().target(RenderLayer.TRANSLUCENT_TARGET).build(false))

    fun init() {}
}