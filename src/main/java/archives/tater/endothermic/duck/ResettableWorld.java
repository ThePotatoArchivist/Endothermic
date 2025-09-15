package archives.tater.endothermic.duck;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public interface ResettableWorld {
    boolean endothermic$reset(RegistryKey<World> worldKey);
}
