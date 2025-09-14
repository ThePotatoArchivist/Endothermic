package archives.tater.endothermic.mixin.indestructible;

import archives.tater.endothermic.registry.EndothermicItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    private int itemAge;

    @Shadow
    @Final
    private static int NEVER_DESPAWN_AGE;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
	private void preventVoid(CallbackInfo info) {
        if (!getStack().isIn(EndothermicItems.INDESTRUCTIBLE) || !(getY() < getWorld().getBottomY())) return;
        setPosition(getX(), getWorld().getBottomY(), getZ());
        setNoGravity(true);
        setVelocity(getVelocity().multiply(0, -0.5, 0));
        setGlowing(true);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V",
            at = @At(value = "TAIL")
    )
    private void preventAge(World world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (getStack().isIn(EndothermicItems.INDESTRUCTIBLE))
            itemAge = NEVER_DESPAWN_AGE;
    }
}