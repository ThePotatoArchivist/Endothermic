package archives.tater.endothermic.mixin.gateways;

import archives.tater.endothermic.registry.EndothermicDataAttachments;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Shadow
    @Final
    private ObjectArrayList<Integer> gateways;

    @Inject(
            method = "dragonKilled",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;dragonKilled:Z")
    )
    private void updateGatewaysOpen(EnderDragonEntity dragon, CallbackInfo ci) {
        world.setAttached(EndothermicDataAttachments.GATEWAYS_OPEN, Unit.INSTANCE);
    }

    @Inject(
            method = "setSpawnState",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;dragonKilled:Z")
    )
    private void updateGatewaysOpen(EnderDragonSpawnState spawnState, CallbackInfo ci) {
        world.removeAttached(EndothermicDataAttachments.GATEWAYS_OPEN);
    }

    @WrapOperation(
            method = "dragonKilled",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonFight;generateNewEndGateway()V")
    )
    private void generateAllGateways(EnderDragonFight instance, Operation<Void> original) {
        var count = gateways.size();
        for (var i = 0; i < count; i++)
            original.call(instance);
    }
}
