@file:Suppress("NOTHING_TO_INLINE", "UnstableApiUsage")

package archives.tater.endothermic.util

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LazyEntityReference
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.function.Function
import kotlin.reflect.KProperty
import com.mojang.datafixers.util.Pair as DFPair
import net.minecraft.util.Pair as MCPair
import net.minecraft.util.Unit as MCUnit

inline operator fun <T, R> Function<T, R>.invoke(arg: T): R = apply(arg)

inline operator fun <T> AttachmentTarget.set(type: AttachmentType<T>, value: T?) {
    setAttached(type, value)
}
inline operator fun <T> AttachmentTarget.get(type: AttachmentType<T>) = getAttached(type)
inline operator fun AttachmentTarget.contains(type: AttachmentType<*>) = hasAttached(type)
fun AttachmentTarget.putAttached(type: AttachmentType<MCUnit>) {
    setAttached(type, MCUnit.INSTANCE)
}

inline operator fun <T> RegistryEntryLookup.RegistryLookup.get(registryRef: RegistryKey<Registry<T>>): RegistryEntryLookup<T> =
    getOrThrow(registryRef)
inline operator fun <T> RegistryEntryLookup<T>.get(key: RegistryKey<T>): RegistryEntry.Reference<T> =
    getOrThrow(key)

inline operator fun <T> MCPair<T, *>.component1(): T = left
inline operator fun <T> MCPair<*, T>.component2(): T = right

inline operator fun <T> DFPair<T, *>.component1(): T = first
inline operator fun <T> DFPair<*, T>.component2(): T = second

inline operator fun <T> TrackedData<T>.getValue(thisRef: Entity, property: KProperty<*>): T =
    thisRef.dataTracker.get(this)
inline operator fun <T> TrackedData<T>.setValue(thisRef: Entity, property: KProperty<*>, value: T) =
    thisRef.dataTracker.set(this, value)

inline operator fun <reified E: Entity> TrackedData<LazyEntityReference<E>>.getValue(thisRef: Entity, property: KProperty<*>): E? =
    thisRef.dataTracker.get(this).resolve(thisRef.world)
inline operator fun <E: Entity> TrackedData<LazyEntityReference<E>>.setValue(thisRef: Entity, property: KProperty<*>, value: E) =
    thisRef.dataTracker.set(this, LazyEntityReference(value.uuid))

inline operator fun Vec3d.minus(other: Vec3d): Vec3d = subtract(other)

inline fun <reified E: Entity, T: Any> registerTrackedData(handler: TrackedDataHandler<T>): TrackedData<T> =
    DataTracker.registerData(E::class.java, handler)

inline fun <reified E: Entity> LazyEntityReference<E>.resolve(world: World) = resolve(world, E::class.java)

inline operator fun World.set(pos: BlockPos, state: BlockState) {
    setBlockState(pos, state)
}
inline operator fun BlockView.get(pos: BlockPos): BlockState = getBlockState(pos)

//context(world: World, pos: BlockPos)
//inline operator fun <T: Comparable<T>> BlockState.set(property: Property<T>, value: T) {
//    world[pos] = this.with(property, value)
//}

inline infix fun BlockState.isOf(block: Block) = this.isOf(block)