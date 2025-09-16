package archives.tater.endothermic.mixin.enchantment;

import archives.tater.endothermic.registry.EndothermicEnchantments;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z"),
            index = 0
    )
    private DamageSource replaceDamageType(DamageSource source, @Local(argsOnly = true) Entity target) {
        if (!(getWorld() instanceof ServerWorld serverWorld)) return source;

        var newType = new MutableObject<RegistryEntry<DamageType>>();

        EnchantmentHelper.forEachEnchantment(this, (enchantment, level, context) ->
                Enchantment.applyEffects(
                        enchantment.value().getEffect(EndothermicEnchantments.REPLACE_DAMAGE_TYPE),
                        Enchantment.createEnchantedDamageLootContext(serverWorld, level, target, source),
                        newType::setValue
                )
        );

        if (newType.getValue() == null) return source;

        if (source.getAttacker() == null && source.getSource() == null)
            if (source.getPosition() == null)
                return new DamageSource(newType.getValue());
            else
                return new DamageSource(newType.getValue(), source.getPosition());
        else
            return new DamageSource(newType.getValue(), source.getSource(), source.getAttacker());
    }
}
