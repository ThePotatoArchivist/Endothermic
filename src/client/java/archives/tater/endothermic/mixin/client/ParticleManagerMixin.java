package archives.tater.endothermic.mixin.client;

import archives.tater.endothermic.client.render.EndothermicRenderLayers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;")
    )
    private static List<ParticleTextureSheet> addParticleSheet(List<ParticleTextureSheet> original) {
        var list = new ArrayList<>(original);
        list.add(EndothermicRenderLayers.PARTICLE_SHEET_ADDITIVE);
        return List.copyOf(list);
    }
}