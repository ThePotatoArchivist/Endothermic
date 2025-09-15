package archives.tater.endothermic.mixin.gateways;

import archives.tater.endothermic.Endothermic;
import archives.tater.endothermic.registry.EndothermicDataAttachments;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(EndGatewayBlock.class)
public class EndGatewayBlockMixin {
    @Inject(
            method = "onEntityCollision",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void checkGatewaysOpen(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld && !Endothermic.isDragonKilled(serverWorld)) ci.cancel();
    }

    @Inject(
            method = "randomDisplayTick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkGatewaysOpen(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!world.hasAttached(EndothermicDataAttachments.GATEWAYS_OPEN)) ci.cancel();
    }
}
