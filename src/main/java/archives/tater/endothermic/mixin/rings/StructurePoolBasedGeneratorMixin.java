package archives.tater.endothermic.mixin.rings;

import archives.tater.endothermic.registry.EndothermicWorldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

@Mixin(StructurePoolBasedGenerator.class)
public class StructurePoolBasedGeneratorMixin {
    @ModifyExpressionValue(
            method = "generate(Lnet/minecraft/world/gen/structure/Structure$Context;Lnet/minecraft/registry/entry/RegistryEntry;Ljava/util/Optional;ILnet/minecraft/util/math/BlockPos;ZLjava/util/Optional;ILnet/minecraft/structure/pool/alias/StructurePoolAliasLookup;Lnet/minecraft/world/gen/structure/DimensionPadding;Lnet/minecraft/structure/StructureLiquidSettings;)Ljava/util/Optional;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockRotation;random(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/util/BlockRotation;")
    )
    private static BlockRotation lockRotation(BlockRotation original, @Local(argsOnly = true) RegistryEntry<StructurePool> structurePool, @Local(argsOnly = true) BlockPos pos) {
        if (!structurePool.isIn(EndothermicWorldgen.CENTER_FUNNELING)) return original;

        var chunkPos =  new ChunkPos(pos);

        if (chunkPos.x == 0) return pos.getZ() > 0 ? BlockRotation.CLOCKWISE_180 : BlockRotation.NONE;
        if (chunkPos.z == 0) return pos.getX() > 0 ? BlockRotation.CLOCKWISE_90 : BlockRotation.COUNTERCLOCKWISE_90;

        if (pos.getX() > pos.getZ() ^ pos.getX() > -pos.getZ()) {
            if (pos.getX() > 0)
                return BlockRotation.CLOCKWISE_90;
            else
                return BlockRotation.COUNTERCLOCKWISE_90;
        } else {
            if (pos.getZ() > 0)
                return BlockRotation.CLOCKWISE_180;
            else
                return BlockRotation.NONE;
        }
    }
}
