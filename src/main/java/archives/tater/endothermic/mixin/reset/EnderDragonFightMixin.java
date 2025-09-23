package archives.tater.endothermic.mixin.reset;

import archives.tater.endothermic.state.EndResetState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin {

    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    private @Nullable BlockPos exitPortalLocation;

    @Inject(
            method = "respawnDragon(Ljava/util/List;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void resetIfEgg(List<EndCrystalEntity> crystals, CallbackInfo ci) {
        if (exitPortalLocation == null) return;
        var eggPos = exitPortalLocation.up(4);
        if (!world.getBlockState(eggPos).isOf(Blocks.DRAGON_EGG)) return;
        EndResetState.startReset(world);
        EndResetState.spawnEggEntity(world, eggPos, crystals);
        ci.cancel();
    }
}
