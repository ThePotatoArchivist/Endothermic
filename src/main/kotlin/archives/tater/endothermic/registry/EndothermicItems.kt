package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object EndothermicItems {
    fun tagOf(id: Identifier): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id)
    fun tagOf(path: String) = tagOf(Endothermic.id(path))

    @JvmField
    val INDESTRUCTIBLE = tagOf("indestructible")

    fun init() {
    }
}