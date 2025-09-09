package archives.tater.endothermic.mixin.indestructible;

import archives.tater.endothermic.registry.EndothermicItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getStack();

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
}