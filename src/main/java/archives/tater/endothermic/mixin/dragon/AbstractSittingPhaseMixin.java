package archives.tater.endothermic.mixin.dragon;

import archives.tater.endothermic.registry.EndothermicDamageTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSittingPhase.class)
public class AbstractSittingPhaseMixin {
    @ModifyExpressionValue(
            method = "modifyDamageTaken",
            at = @At(value = "CONSTANT", args = "classValue=net/minecraft/entity/projectile/PersistentProjectileEntity")
    )
    private boolean checkPlayerProjectileDamage(boolean original, @Local(argsOnly = true) DamageSource source) {
        return original || source.isOf(EndothermicDamageTypes.DASH_ATTACK);
    }
}
