package archives.tater.endothermic.mixin.enchantment;

import archives.tater.endothermic.registry.EndothermicEnchantments;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import org.apache.commons.lang3.mutable.MutableFloat;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void addMotionParticles(CallbackInfo ci) {
        if (!getWorld().isClient) return;
        EnchantmentHelper.forEachEnchantment((LivingEntity) (Object) this, (enchantment, level, context) -> {
            var effect = enchantment.value().effects().get(EndothermicEnchantments.MOTION_PARTICLES);
            if (effect == null) return;
            effect.tick((LivingEntity) (Object) this, level);
        });
    }

    @ModifyReturnValue(
            method = "modifyAppliedDamage",
            at = @At("TAIL")
    )
    private float modifyDamageTaken(float original, @Local(argsOnly = true) DamageSource source) {
        if (!(getWorld() instanceof ServerWorld world)) return original;
        var damage = new MutableFloat(original);

        EnchantmentHelper.forEachEnchantment((LivingEntity) (Object) this, (enchantment, level, context) -> {
            Enchantment.applyEffects(
                    enchantment.value().getEffect(EndothermicEnchantments.DAMAGE_TAKEN),
                    Enchantment.createEnchantedDamageLootContext(world, level, this, source),
                    effect -> damage.setValue(effect.apply(level, getRandom(), damage.floatValue()))
            );
        });

        return damage.floatValue();
    }
}
