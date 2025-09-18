package archives.tater.endothermic.mixin.dragon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
