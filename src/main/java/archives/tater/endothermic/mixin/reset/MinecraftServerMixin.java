package archives.tater.endothermic.mixin.reset;

import archives.tater.endothermic.Endothermic;
import archives.tater.endothermic.duck.ResettableWorld;
import com.google.common.collect.ImmutableList;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressLogger;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ResettableWorld {
    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;

    @Shadow
    @Final
    private Executor workerExecutor;

    @Shadow
    @Final
    protected LevelStorage.Session session;

    @Shadow
    @Final
    private CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries;

    @Shadow
    @Final
    protected SaveProperties saveProperties;

    @Override
    public boolean endothermic$reset(RegistryKey<World> worldKey) {
        var existing = worlds.get(worldKey);
        if (existing == null) return false;

        try {
            existing.close();
        } catch (IOException e) {
            Endothermic.logger.error("Error when closing world for reset: ", e);
            return false;
        }

        try {
            Files.walkFileTree(session.getWorldDirectory(worldKey), new SimpleFileVisitor<>() {
                @Override
                public @NotNull FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }

                @Override
                public @NotNull FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        } catch (IOException e) {
            Endothermic.logger.error("Error when deleting world for reset: ", e);
            return false;
        }

        var replacement = new ServerWorld(
                (MinecraftServer) (Object) this,
                workerExecutor,
                session,
                new UnmodifiableLevelProperties(saveProperties, saveProperties.getMainWorldProperties()),
                worldKey,
                combinedDynamicRegistries.getCombinedRegistryManager().getOrThrow(RegistryKeys.DIMENSION).get(worldKey.getValue()),
                WorldGenerationProgressLogger.noSpawnChunks(),
                false,
                existing.getSeed(),
                ImmutableList.of(),
                false,
                existing.getRandomSequences()
        );
        worlds.put(worldKey, replacement);
        return true;
    }
}
