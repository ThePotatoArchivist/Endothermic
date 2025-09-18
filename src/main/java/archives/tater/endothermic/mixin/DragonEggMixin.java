package archives.tater.endothermic.mixin;

import archives.tater.endothermic.payload.EggTeleportPayload;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonEggBlock.class)
public class DragonEggMixin {
    @Inject(
            method = "teleport",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableClient(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (world.isClient) ci.cancel();
    }

    @Inject(
            method = "teleport",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
    )
    private void sendPayload(BlockState state, World world, BlockPos pos, CallbackInfo ci, @Local(ordinal = 1) BlockPos destination) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        for (var player : world.getPlayers()) {
            serverWorld.sendToPlayerIfNearby((ServerPlayerEntity) player, false, pos.getX(), pos.getY(), pos.getZ(), new CustomPayloadS2CPacket(new EggTeleportPayload(pos, destination)));
        }
    }
}
