package archives.tater.endothermic.mixin.gateways;

import archives.tater.endothermic.registry.EndothermicDataAttachments;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(method = "getCollisionShape", at = @At(value = "FIELD", target = "Lnet/minecraft/block/AbstractBlock;collidable:Z"))
    private boolean collideIfGatewaysClosed(boolean original, @Local(argsOnly = true) ShapeContext context) {
        return original ||
                (Object) this instanceof EndGatewayBlock
                        && context instanceof EntityShapeContext entityShapeContext
                        && entityShapeContext.getEntity() != null
                        && !entityShapeContext.getEntity().getWorld().hasAttached(EndothermicDataAttachments.GATEWAYS_OPEN);
    }
}
