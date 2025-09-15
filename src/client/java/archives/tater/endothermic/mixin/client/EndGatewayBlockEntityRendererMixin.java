package archives.tater.endothermic.mixin.client;

import archives.tater.endothermic.registry.EndothermicDataAttachments;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.EndGatewayBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(EndGatewayBlockEntityRenderer.class)
public class EndGatewayBlockEntityRendererMixin {
    @Unique
    private BlockRenderManager blockRenderer;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void getBlockRenderer(BlockEntityRendererFactory.Context context, CallbackInfo ci) {
        blockRenderer = context.getRenderManager();
    }

    @Inject(
            method = "render(Lnet/minecraft/block/entity/EndGatewayBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void noRenderIfGatewaysClosed(EndGatewayBlockEntity endGatewayBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, Vec3d vec3d, CallbackInfo ci) {
        var world = endGatewayBlockEntity.getWorld();
        if (world == null || world.hasAttached(EndothermicDataAttachments.GATEWAYS_OPEN)) return;

        blockRenderer.renderBlockAsEntity(Blocks.BEDROCK.getDefaultState(), matrixStack, vertexConsumerProvider, i, j, world, endGatewayBlockEntity.getPos());

        ci.cancel();
    }
}
