package archives.tater.endothermic.util

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import java.util.function.Consumer
import java.util.function.Supplier
import com.mojang.datafixers.util.Pair as DFPair

fun <R> dataResultError(partialResult: R, message: Supplier<String>): DataResult<R> =
    DataResult.error<R>(message, partialResult)

class PairListCodec<T1, T2>(private val first: Codec<T1>, private val second: Codec<T2>) : Codec<DFPair<T1, T2>> {
    override fun <T> encode(input: DFPair<T1, T2>, ops: DynamicOps<T>, prefix: T): DataResult<T> =
        ops.listBuilder().apply {
            add(first.encodeStart(ops, input.first))
            add(second.encodeStart(ops, input.second))
        }.build(prefix)

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<DFPair<DFPair<T1, T2>, T>> =
        ops.getList(input).flatMap { stream ->
            DecoderState(ops).also {
                stream.accept(it)
            }.result(input)
        }

    private inner class DecoderState<T>(private val ops: DynamicOps<T>) : Consumer<T> {
        lateinit var firstValue: DataResult<DFPair<T1, T>>
        lateinit var secondValue: DataResult<DFPair<T2, T>>
        val extra = mutableListOf<T>()

        override fun accept(input: T) {
            when {
                !::firstValue.isInitialized -> firstValue = first.decode(ops, input)
                !::secondValue.isInitialized -> secondValue = second.decode(ops, input)
                else -> extra.add(input)
            }
        }

        fun result(input: T): DataResult<DFPair<DFPair<T1, T2>, T>> {
            val firstValue = firstValue
            val secondValue = secondValue

            return when {
                !::firstValue.isInitialized -> DataResult.error { "List was empty" }
                !::secondValue.isInitialized -> DataResult.error { "List only contained one element" }
                firstValue is DataResult.Error && !firstValue.hasResultOrPartial() -> DataResult.error { "First element failed: ${firstValue.message()}" }
                secondValue is DataResult.Error && !secondValue.hasResultOrPartial() -> DataResult.error { "Second element failed: ${secondValue.message()}" }
                else -> {
                    val pair = DFPair(DFPair(firstValue.resultOrPartial().orElseThrow().first, secondValue.resultOrPartial().orElseThrow().first), input)
                    when {
                        firstValue is DataResult.Error -> dataResultError(pair) { "First element errored: ${firstValue.message()}" }
                        secondValue is DataResult.Error -> dataResultError(pair) { "First element errored: ${secondValue.message()}" }
                        extra.isNotEmpty() -> dataResultError(pair) { "List contained extra elements: $extra" }
                        else -> DataResult.success(pair)
                    }
                }
            }
        }
    }
}