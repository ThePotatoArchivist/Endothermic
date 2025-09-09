package archives.tater.endothermic.mixin.dashrocket;

import archives.tater.endothermic.item.DashRocketItem;
import archives.tater.endothermic.registry.EndothermicDamageTypes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static archives.tater.endothermic.registry.EndothermicDataAttachments.DASH_TICKS;

@SuppressWarnings("UnstableApiUsage")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract boolean isGliding();

    @ModifyVariable(
            method = "checkGlidingCollision",
            at = @At("STORE")
    )
    private float multiplyWallDamage(float value) {
        return hasAttached(DASH_TICKS) ? DashRocketItem.WALL_DAMAGE_MULTIPLIER * value : value;
    }

    @ModifyReturnValue(
            method = "isInvulnerableTo",
            at = @At("RETURN")
    )
    private boolean invulnerable(boolean original, @Local(argsOnly = true)DamageSource damageSource) {
        return original || hasAttached(DASH_TICKS) && !damageSource.isIn(EndothermicDamageTypes.BYPASSES_DASH);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void tickDash(CallbackInfo ci) {
        if (isRemoved() || !hasAttached(DASH_TICKS)) return;

        if (!isGliding()) {
            if (!getWorld().isClient)
                removeAttached(DASH_TICKS);
            return;
        }

        for (var i = 0; i < 16; i++)
            getWorld().addParticleClient(ParticleTypes.FIREWORK, getX() + 2 * random.nextDouble() - 1, getY() + 2 * random.nextDouble() - 1, getZ() + 2 * random.nextDouble() - 1, 0, 0, 0);

        var rotation = getRotationVector();
        double d = 1.5;
        double e = 0.1;
        var velocity = getVelocity();
        setVelocity(velocity.add(rotation.x * e + (rotation.x * d - velocity.x) * 0.5, rotation.y * e + (rotation.y * d - velocity.y) * 0.5, rotation.z * e + (rotation.z * d - velocity.z) * 0.5));

        if (getWorld().isClient) return;

        var dashTicksNew = getAttachedOrElse(DASH_TICKS, 0) - 1;
        if (dashTicksNew <= 0)
            removeAttached(DASH_TICKS);
        else
            setAttached(DASH_TICKS, dashTicksNew);
    }
}
