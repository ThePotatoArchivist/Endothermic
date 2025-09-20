package archives.tater.endothermic.mixin.dragon;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HoldingPatternPhase.class)
public abstract class HoldingPatternPhaseMixin extends AbstractPhase {
    public HoldingPatternPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
    }

    @WrapOperation(
            method = "tickInRange",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/phase/HoldingPatternPhase;strafePlayer(Lnet/minecraft/entity/player/PlayerEntity;)V")
    )
    private void useDiveAttack(HoldingPatternPhase instance, PlayerEntity player, Operation<Void> original) {
        if (dragon.getRandom().nextInt(2) == 0) {
            original.call(instance, player);
            return;
        }
        dragon.getPhaseManager().setPhase(PhaseType.CHARGING_PLAYER);
        dragon.getPhaseManager().create(PhaseType.CHARGING_PLAYER).setPathTarget(player.getPos());
    }
}
