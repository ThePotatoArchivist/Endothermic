package archives.tater.endothermic.mixin.dragon;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExplosiveProjectileEntity.class)
public class ExplosiveProjectileEntityMixin {
    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(
            method = "canHit",
            at = @At("RETURN")
    )
    private boolean preventDragonHittingSelf(boolean original, @Local(argsOnly = true) Entity entity) {
        return original && (!((Object) this instanceof DragonFireballEntity) || entity.getType() != EntityType.ENDER_DRAGON);
    }
}
