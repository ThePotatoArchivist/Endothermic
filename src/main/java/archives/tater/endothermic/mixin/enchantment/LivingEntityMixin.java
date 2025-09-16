package archives.tater.endothermic.mixin.enchantment;

import archives.tater.endothermic.registry.EndothermicEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
