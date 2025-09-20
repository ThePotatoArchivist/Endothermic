package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.block.GlideBoosterBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import java.util.function.Function

object EndothermicBlocks {

    private fun register(id: Identifier, block: Function<AbstractBlock.Settings, Block> = Function(::Block), settings: AbstractBlock.Settings = AbstractBlock.Settings.create()): Block =
        Blocks.register(RegistryKey.of(RegistryKeys.BLOCK, id), block, settings)

    private inline fun register(id: Identifier, block: Function<AbstractBlock.Settings, Block> = Function(::Block), settings: AbstractBlock.Settings.() -> Unit) =
        register(id, block, AbstractBlock.Settings.create().apply(settings))

    private inline fun register(path: String, block: Function<AbstractBlock.Settings, Block> = Function(::Block), settings: AbstractBlock.Settings.() -> Unit) =
        register(Endothermic.id(path), block, settings)

    val GLIDE_BOOSTER = register("glide_booster", ::GlideBoosterBlock) {
        noCollision()
    }

    fun init() {

    }
}