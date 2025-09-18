package archives.tater.endothermic.mixin.enchantment;

import archives.tater.endothermic.registry.EndothermicEnchantments;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Shadow
    public static void forEachEnchantment(LivingEntity entity, EnchantmentHelper.ContextAwareConsumer contextAwareConsumer) {
    }

    @Unique
    private static void customOnTargetDamaged(
            Enchantment enchantment, ServerWorld world, int level, EnchantmentEffectContext context, EnchantmentEffectTarget target, Entity user, DamageSource damageSource
    ) {
        for (var targetedEnchantmentEffect : enchantment.getEffect(EndothermicEnchantments.POST_ATTACK)) {
            if (target != targetedEnchantmentEffect.enchanted()) continue;
            Enchantment.applyTargetedEffect(targetedEnchantmentEffect, world, level, context, user, damageSource);
        }
    }

    @Inject(
            method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)V",
            at = @At("TAIL")
    )
    private static void customPostAttack(ServerWorld world, Entity target, DamageSource damageSource, @Nullable ItemStack weapon, Consumer<Item> breakCallback, CallbackInfo ci) {
        if (damageSource.getAttacker() instanceof LivingEntity attacker)
            forEachEnchantment(
                    attacker,
                    (enchantment, level, context) -> customOnTargetDamaged(enchantment.value(), world, level, context, EnchantmentEffectTarget.ATTACKER, target, damageSource)
            );
        if (target instanceof LivingEntity victim)
            forEachEnchantment(
                    victim,
                    (enchantment, level, context) -> customOnTargetDamaged(enchantment.value(), world, level, context, EnchantmentEffectTarget.VICTIM, target, damageSource)
            );
    }

    @Inject(
            method = "getDamage",
            at = @At("TAIL")
    )
    private static void velocityScaledDamage(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseDamage, CallbackInfoReturnable<Float> cir, @Local MutableFloat damage) {
        if (!(damageSource.getAttacker() instanceof LivingEntity attacker)) return;
        forEachEnchantment(attacker, (enchantment, level, context) -> {
            Enchantment.applyEffects(enchantment.value().getEffect(EndothermicEnchantments.VELOCITY_SCALED_DAMAGE), Enchantment.createEnchantedDamageLootContext(world, level, attacker, damageSource), effect -> {
                damage.setValue(damage.getValue() * effect.getMultiplier(level, attacker.getMovement()));
            });
        });
    }
}
