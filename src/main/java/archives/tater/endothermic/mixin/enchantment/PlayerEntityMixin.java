package archives.tater.endothermic.mixin.enchantment;

import archives.tater.endothermic.registry.EndothermicEnchantments;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static java.util.Objects.requireNonNull;

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

        var type = new MutableObject<>(source.getTypeRegistryEntry());

        EnchantmentHelper.forEachEnchantment(this, (enchantment, level, context) ->
                Enchantment.applyEffects(
                        enchantment.value().getEffect(EndothermicEnchantments.REPLACE_DAMAGE_TYPE),
                        Enchantment.createEnchantedDamageLootContext(serverWorld, level, target, source),
                        type::setValue
                )
        );

        var proxyType = new MutableObject<EntityType<?>>();

        EnchantmentHelper.forEachEnchantment(this, (enchantment, level, context) ->
                Enchantment.applyEffects(
                        enchantment.value().getEffect(EndothermicEnchantments.PROXY_DAMAGE_SOURCE),
                        Enchantment.createEnchantedDamageLootContext(serverWorld, level, target, source),
                        proxyType::setValue
                )
        );

        if (type.getValue() == source.getTypeRegistryEntry() && proxyType.getValue() == null) return source;

        Entity sourceEntity;
        if (proxyType.getValue() == null) {
            sourceEntity = source.getSource();
        } else {
            sourceEntity = requireNonNull(proxyType.getValue().create(getWorld(), SpawnReason.SPAWN_ITEM_USE));
            if (sourceEntity instanceof ProjectileEntity projectileEntity)
                projectileEntity.setOwner(this);
            sourceEntity.setPosition(getPos());
            sourceEntity.setVelocity(getVelocity());
            sourceEntity.setAngles(getYaw(), getPitch());
            sourceEntity.setPitch(getPitch());
            sourceEntity.setHeadYaw(getHeadYaw());
            sourceEntity.setBodyYaw(getBodyYaw());
        }

        if (source.getAttacker() == null && sourceEntity == null)
            if (source.getPosition() == null)
                return new DamageSource(type.getValue());
            else
                return new DamageSource(type.getValue(), source.getPosition());
        else
            return new DamageSource(type.getValue(), sourceEntity, source.getAttacker());
    }

    @WrapOperation(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;sidedDamage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    )
    private boolean cancelIfDeflected(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (source.getSource() instanceof ProjectileEntity projectileEntity && instance.getProjectileDeflection(projectileEntity) != ProjectileDeflection.NONE)
            return false;
        return original.call(instance, source, amount);
    }
}
