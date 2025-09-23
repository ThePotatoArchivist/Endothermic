package archives.tater.endothermic.mixin.dragon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {
    @ModifyExpressionValue(
            method = "tickMovement",
            at = @At(value = "CONSTANT", args = "doubleValue=0.01")
    )
    private double changeScaling(double original) {
        return 10 * original;
    }
}
