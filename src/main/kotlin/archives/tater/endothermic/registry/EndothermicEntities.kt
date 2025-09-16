package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.entity.FloatingDragonEggEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object EndothermicEntities {
    private fun <T: Entity> register(key: RegistryKey<EntityType<*>>, type: EntityType.Builder<T>): EntityType<T> =
        Registry.register(Registries.ENTITY_TYPE, key, type.build(key))

    private inline fun <T: Entity> register(id: Identifier, factory: EntityType.EntityFactory<T>, spawnGroup: SpawnGroup = SpawnGroup.MISC, init: EntityType.Builder<T>.() -> Unit): EntityType<T> =
        register(RegistryKey.of(RegistryKeys.ENTITY_TYPE, id), EntityType.Builder.create(factory, spawnGroup).apply(init))

    private inline fun <T: Entity> register(path: String, factory: EntityType.EntityFactory<T>, spawnGroup: SpawnGroup = SpawnGroup.MISC, init: EntityType.Builder<T>.() -> Unit) =
        register(Endothermic.id(path), factory, spawnGroup, init)

    private inline fun <T: Entity> registerProjectile(path: String, factory: EntityType.EntityFactory<T>, init: EntityType.Builder<T>.() -> Unit = {}) =
        register(path, factory) {
            dimensions(0.25f, 0.25f)
            maxTrackingRange(4)
            trackingTickInterval(10)
            init()
        }

    private fun tagOf(id: Identifier): TagKey<EntityType<*>> = TagKey.of(RegistryKeys.ENTITY_TYPE, id)
    private fun tagOf(path: String) = tagOf(Endothermic.id(path))

    val FLOATING_DRAGON_EGG = register("floating_dragon_egg", ::FloatingDragonEggEntity) {
        dimensions(1f, 1f)
        maxTrackingRange(8)
        trackingTickInterval(Integer.MAX_VALUE)
    }

    val END_NATIVE = tagOf("end_native")

    fun init() {

    }
}