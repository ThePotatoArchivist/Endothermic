package archives.tater.endothermic.mixin.portals;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(EndGatewayBlockEntity.class)
public interface EndGatewayBlockEntityInvoker {
    @Invoker
    static BlockPos invokeSetupExitPortalLocation(ServerWorld world, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker
    static BlockPos invokeFindBestPortalExitPos(World world, BlockPos pos) {
        throw new AssertionError();
    }
}
