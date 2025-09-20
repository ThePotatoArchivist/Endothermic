package archives.tater.endothermic.util

import kotlin.enums.EnumEntries
import kotlin.enums.enumEntries

class SealedMap<K : Enum<K>, V> private constructor(private val map: Map<K, V>) : Map<K, V> by map {
    constructor(entries: EnumEntries<K>, map: Map<K, V>) : this(map) {
        if (!entries.all { it in map })
            throw AssertionError("Not all entries were contained in map")
    }

    constructor(entries: EnumEntries<K>, valueSelector: (K) -> V) : this(entries.associateWith(valueSelector))

    override fun get(key: K): V = map[key]!!

    @Deprecated("Use get() instead, defaultValue will never be used", replaceWith = ReplaceWith("this[key]"))
    override fun getOrDefault(key: K, defaultValue: V): V = this[key]
}

inline fun <reified K : Enum<K>, V> sealedMapOf(vararg entries: Pair<K, V>) =
    SealedMap(enumEntries<K>(), mapOf(*entries))
inline fun <reified K : Enum<K>, V> sealedMapOf(noinline valueSelector: (K) -> V) =
    SealedMap(enumEntries<K>(), valueSelector)
