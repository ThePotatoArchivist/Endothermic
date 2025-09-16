package archives.tater.endothermic.enchantment.effect

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.enchantment.EnchantmentLevelBasedValue
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class VelocityScaledDamageEnchantmentEffect(
    val multiplierPerVelocity: EnchantmentLevelBasedValue,
    val minMultiplier: Optional<EnchantmentLevelBasedValue>,
    val maxMultiplier: Optional<EnchantmentLevelBasedValue>,
) {
    constructor(multiplierPerVelocity: EnchantmentLevelBasedValue, minMultiplier: EnchantmentLevelBasedValue? = null, maxMultiplier: EnchantmentLevelBasedValue? = null) :
            this(multiplierPerVelocity, Optional.ofNullable(minMultiplier), Optional.ofNullable(maxMultiplier))

    fun getMultiplier(level: Int, velocity: Vec3d) = (multiplierPerVelocity.getValue(level) * velocity.length().toFloat()).coerceIn(
        minMultiplier.getOrNull()?.getValue(level) ?: Float.MIN_VALUE,
        maxMultiplier.getOrNull()?.getValue(level) ?: Float.MAX_VALUE
    )

    companion object {
        val CODEC: Codec<VelocityScaledDamageEnchantmentEffect> = RecordCodecBuilder.create { it.group(
            EnchantmentLevelBasedValue.CODEC.fieldOf("multiplier_per_velocity").forGetter(VelocityScaledDamageEnchantmentEffect::multiplierPerVelocity),
            EnchantmentLevelBasedValue.CODEC.optionalFieldOf("min_multiplier").forGetter(VelocityScaledDamageEnchantmentEffect::minMultiplier),
            EnchantmentLevelBasedValue.CODEC.optionalFieldOf("max_multiplier").forGetter(VelocityScaledDamageEnchantmentEffect::maxMultiplier),
        ).apply(it, ::VelocityScaledDamageEnchantmentEffect) }
    }
}