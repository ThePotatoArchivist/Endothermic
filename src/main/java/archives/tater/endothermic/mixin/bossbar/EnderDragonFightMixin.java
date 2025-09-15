package archives.tater.endothermic.mixin.bossbar;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Shadow
    private boolean dragonKilled;
    @Shadow
    private int endCrystalsAlive;
    @Unique
    private final ServerBossBar endCrystalBossbar = new ServerBossBar(Text.of("End Crystals"), BossBar.Color.PURPLE, BossBar.Style.NOTCHED_10); // TODO translation

    @Inject(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/ServerBossBar;setVisible(Z)V", shift = At.Shift.AFTER)
    )
    private void setVisible(CallbackInfo ci) {
        endCrystalBossbar.setVisible(!dragonKilled);
    }

    @WrapOperation(
            method = "updatePlayers",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/ServerBossBar;addPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;)V"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/ServerBossBar;removePlayer(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
            }
    )
    private void updatePlayers(ServerBossBar instance, ServerPlayerEntity player, Operation<Void> original) {
        original.call(instance, player);
        original.call(endCrystalBossbar, player);
    }

    @Inject(
            method = "dragonKilled",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/ServerBossBar;setPercent(F)V")
    )
    private void resetBar(EnderDragonEntity dragon, CallbackInfo ci) {
        endCrystalBossbar.setPercent(0f);
        endCrystalBossbar.setVisible(false);
    }

    @Inject(
            method = "countAliveCrystals",
            at = @At("TAIL")
    )
    private void updatePercent(CallbackInfo ci) {
        endCrystalBossbar.setPercent(endCrystalsAlive / 10f);
    }
}
