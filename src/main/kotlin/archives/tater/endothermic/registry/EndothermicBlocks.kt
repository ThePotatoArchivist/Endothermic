package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.block.VelocititeFieldBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import java.util.function.Function

object EndothermicBlocks {

    private fun register(id: Identifier, block: Function<AbstractBlock.Settings, Block> = Function(::Block), settings: AbstractBlock.Settings = AbstractBlock.Settings.create()): Block =
        Blocks.register(RegistryKey.of(RegistryKeys.BLOCK, id), block, settings)

    private inline fun register(id: Identifier, block: Function<AbstractBlock.Settings, Block> = Function(::Block), settings: AbstractBlock.Settings.() -> Unit) =
        register(id, block, AbstractBlock.Settings.create().apply(settings))

    private inline fun register(path: String, block: Function<AbstractBlock.Settings, Block> = Function(::Block), settings: AbstractBlock.Settings.() -> Unit = {}) =
        register(Endothermic.id(path), block, settings)

    private fun tagOf(path: String): TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, Endothermic.id(path))

    val VELOCITITE = register("velocitite") {
        strength(3f, 9f)
        sounds(BlockSoundGroup.NETHERITE)
    }

    val VELOCITITE_FIELD = register("velocitite_field", ::VelocititeFieldBlock) {
        noCollision()
        strength(-1f, 3600000f)
        dropsNothing()
        luminance { 15 }
        sounds(BlockSoundGroup.GLASS)
        pistonBehavior(PistonBehavior.BLOCK)
    }

    val VELOCITITE_FIELD_BORDER = tagOf("velocitite_field_border")

    fun init() {

    }
}