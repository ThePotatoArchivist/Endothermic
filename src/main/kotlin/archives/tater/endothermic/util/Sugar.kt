@file:Suppress("NOTHING_TO_INLINE")

package archives.tater.endothermic.util

import java.util.function.Function

inline operator fun <T, R> Function<T, R>.invoke(arg: T): R = apply(arg)