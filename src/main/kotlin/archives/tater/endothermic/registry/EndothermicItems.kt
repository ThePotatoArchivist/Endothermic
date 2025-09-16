package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.function.Function

object EndothermicItems {
    private fun register(id: Identifier, item: Function<Item.Settings, Item> = Function(::Item), settings: Item.Settings = Item.Settings()): Item =
        Items.register(RegistryKey.of(RegistryKeys.ITEM, id), item, settings)

    private inline fun register(id: Identifier, item: Function<Item.Settings, Item> = Function(::Item), settings: Item.Settings.() -> Unit) =
        register(id, item, Item.Settings().apply(settings))

    private inline fun register(path: String, item: Function<Item.Settings, Item>, settings: Item.Settings.() -> Unit = {}) =
        register(Endothermic.id(path), item, settings)

    private fun tagOf(id: Identifier): TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id)
    private fun tagOf(path: String) = tagOf(Endothermic.id(path))

    @JvmField
    val INDESTRUCTIBLE = tagOf("indestructible")
    val ELYTRA_ENCHANTABLE = tagOf("enchantable/elytra")

    fun init() {
    }
}