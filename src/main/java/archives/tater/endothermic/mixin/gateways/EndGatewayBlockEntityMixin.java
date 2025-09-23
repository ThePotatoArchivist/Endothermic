package archives.tater.endothermic.mixin.gateways;

import archives.tater.endothermic.registry.EndothermicDataAttachments;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.objectweb.asm.Opcodes;

import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.world.World;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayBlockEntityMixin {
    @SuppressWarnings("UnstableApiUsage")
    @WrapWithCondition(
            method = {
                    "clientTick",
                    "serverTick"
            },
            at = @At(value = "FIELD", target = "Lnet/minecraft/block/entity/EndGatewayBlockEntity;age:J", opcode = Opcodes.PUTFIELD)
    )
    private static boolean noAgeBeforeDragon(EndGatewayBlockEntity instance, long value, @Local(argsOnly = true) World world) {
        return world.hasAttached(EndothermicDataAttachments.GATEWAYS_OPEN);
    }
}
