package archives.tater.endothermic.mixin.portals;

import archives.tater.endothermic.EndPortalCache;
import archives.tater.endothermic.Endothermic;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(
            method = "createTeleportTarget",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/EndPlatformFeature;generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/util/math/BlockPos;Z)V"),
            cancellable = true
    )
    private void sendToOuterEnd(ServerWorld world, Entity entity, BlockPos pos, CallbackInfoReturnable<TeleportTarget> cir, @Local(ordinal = 1) ServerWorld endWorld) {
        if (Endothermic.isDragonKilled(endWorld)) return;
        var cachedPos = endWorld.getPersistentStateManager().getOrCreate(EndPortalCache.TYPE).getOrCreate(endWorld, pos);
        cir.setReturnValue(new TeleportTarget(
                endWorld,
                EndGatewayBlockEntityInvoker.invokeFindBestPortalExitPos(endWorld, cachedPos).toBottomCenterPos(),
                Vec3d.ZERO,
                0.0F,
                0.0F,
                PositionFlag.combine(PositionFlag.DELTA, Set.of(PositionFlag.X_ROT)),
                TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET)
        ));
    }
}
