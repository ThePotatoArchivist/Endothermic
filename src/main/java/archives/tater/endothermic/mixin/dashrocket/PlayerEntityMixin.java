package archives.tater.endothermic.mixin.dashrocket;

import archives.tater.endothermic.item.DashRocketItem;
import archives.tater.endothermic.registry.EndothermicDamageTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static archives.tater.endothermic.registry.EndothermicDataAttachments.DASH_TICKS;
import static java.lang.Math.max;

@SuppressWarnings("UnstableApiUsage")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0)
    )
    private double multiplyAttackDamage(double original) {
        return hasAttached(DASH_TICKS) ? original * DashRocketItem.ATTACK_VELOCITY_COEFFICIENT * max(1, getVelocity().length()) : original;
    }

    @WrapOperation(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSources;playerAttack(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/entity/damage/DamageSource;")
    )
    private DamageSource changeDamageType(DamageSources instance, PlayerEntity attacker, Operation<DamageSource> original) {
        return hasAttached(DASH_TICKS) ? instance.create(EndothermicDamageTypes.DASH_ATTACK, this) : original.call(instance, attacker);
    }

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackKnockbackAgainst(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)F")
    )
    private void addVelocity(Entity target, CallbackInfo ci) {
        if (!hasAttached(DASH_TICKS)) return;
        target.addVelocity(getMovement().multiply(DashRocketItem.VELOCITY_KNOCKBACK_MULTIPLIER));
    }
}
