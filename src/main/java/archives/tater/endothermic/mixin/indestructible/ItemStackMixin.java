package archives.tater.endothermic.mixin.indestructible;

import archives.tater.endothermic.registry.EndothermicItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isOf(Item item);

    @Shadow
    public abstract boolean isIn(TagKey<Item> tag);

    @ModifyReturnValue(
            method = "takesDamageFrom",
            at = @At("RETURN")
    )
    private boolean indestructible(boolean original, @Local(argsOnly = true) DamageSource source) {
        return original && !isIn(EndothermicItems.INDESTRUCTIBLE) || source.isOf(DamageTypes.GENERIC_KILL);
    }
}
