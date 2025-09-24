package archives.tater.endothermic.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.entity.EnderDragonEntityRenderer;

@Mixin(EnderDragonEntityRenderer.class)
public class EnderDragonEntityRendererMixin {
    @ModifyExpressionValue(
            method = "renderCrystalBeam",
            at = @At(value = "CONSTANT", args = "floatValue=0.01")
    )
    private static float invertBeamDirection(float original) {
        return -original;
    }
}
