package archives.tater.endothermic.mixin.enchantment;

import archives.tater.endothermic.registry.EndothermicEnchantments;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Shadow
    public static <T> void applyEffects(List<EnchantmentEffectEntry<T>> enchantmentEffectEntries, LootContext lootContext, Consumer<T> effectConsumer) {}

    @Shadow
    public abstract <T> List<T> getEffect(ComponentType<List<T>> type);

    @Shadow
    public static LootContext createEnchantedDamageLootContext(ServerWorld world, int level, Entity entity, DamageSource damageSource) {
        throw new AssertionError();
    }

    @Inject(
            method = "modifyDamage",
            at = @At("TAIL")
    )
    private void addVelocityScaledDamage(ServerWorld world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat damage, CallbackInfo ci) {
        applyEffects(getEffect(EndothermicEnchantments.VELOCITY_SCALED_DAMAGE), createEnchantedDamageLootContext(world, level, user, damageSource), effect -> {
            damage.setValue(damage.getValue() * effect.getMultiplier(level, user.getMovement()));
        });
    }
}
