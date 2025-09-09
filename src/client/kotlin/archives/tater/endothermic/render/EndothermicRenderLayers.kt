package archives.tater.endothermic.render

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.util.MultiPhaseParameters
import archives.tater.endothermic.util.RenderPipeline
import archives.tater.endothermic.util.withShaders
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DestFactor
import com.mojang.blaze3d.platform.SourceFactor
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import net.minecraft.util.Util.memoize
import java.util.function.Function

object EndothermicRenderLayers {
    val RENDERTYPE_FOUNTAIN: RenderPipeline = RenderPipelines.register(
        RenderPipeline(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET) {
            withLocation(Endothermic.id("pipeline/fountain"))
            withShaders(Endothermic.id("core/rendertype_fountain"))
            withBlend(BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE))
            withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
        }
    )

    val FOUNTAIN: RenderLayer = RenderLayer.of(
        "${Endothermic.MOD_ID}_fountain",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        RENDERTYPE_FOUNTAIN,
        MultiPhaseParameters {
            target(RenderLayer.WEATHER_TARGET)
        }
    )

    val RENDERTYPE_SPARKLE: RenderPipeline = RenderPipelines.register(
        RenderPipeline(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET) {
            withLocation(Endothermic.id("pipeline/sparkle"))
            withShaders(Endothermic.id("core/rendertype_sparkle"))
            withBlend(BlendFunction(SourceFactor.SRC_ALPHA, DestFactor.ONE))
            withSampler("Sampler0")
            withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, VertexFormat.DrawMode.QUADS)
        }
    )

    val SPARKLE: Function<Identifier, RenderLayer> = memoize { texture ->
        RenderLayer.of(
            "${Endothermic.MOD_ID}_sparkle",
            RenderLayer.DEFAULT_BUFFER_SIZE,
            false,
            true,
            RENDERTYPE_SPARKLE,
            MultiPhaseParameters {
                texture(RenderPhase.Texture(texture, false))
                lightmap(RenderPhase.ENABLE_LIGHTMAP)
                overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
            }
        )
    }

    fun init() {}
}