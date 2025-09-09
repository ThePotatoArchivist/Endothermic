@file:Suppress("NOTHING_TO_INLINE", "UnstableApiUsage")

package archives.tater.endothermic.util

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import java.util.function.Function
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
