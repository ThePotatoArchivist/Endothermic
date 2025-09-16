package archives.tater.endothermic.enchantment.effect

import archives.tater.endothermic.registry.EndothermicParticles
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.enchantment.EnchantmentLevelBasedValue
import net.minecraft.entity.LivingEntity
import net.minecraft.predicate.entity.EntityPredicate

data class MotionParticlesEnchantmentEffect(val count: EnchantmentLevelBasedValue, val condition: EntityPredicate) {
    fun tick(entity: LivingEntity, level: Int) {
        if (!condition.test(null, null, entity)) return // May cause crashes due to missing ServerWorld, it is the responsibility of the enchantment author to not use ServerWorld-dependent entity predicates
        repeat(count.getValue(level).toInt()) {
            entity.world.addParticleClient(
                EndothermicParticles.DASH,
                entity.x + 2 * entity.random.nextDouble() - 1,
                entity.y + 2 * entity.random.nextDouble() - 1,
                entity.z + 2 * entity.random.nextDouble() - 1,
                entity.velocity.x,
                entity.velocity.y,
                entity.velocity.z
            )
        }
    }

    companion object {
        val CODEC: Codec<MotionParticlesEnchantmentEffect> = RecordCodecBuilder.create { it.group(
            EnchantmentLevelBasedValue.CODEC.fieldOf("count").forGetter(MotionParticlesEnchantmentEffect::count),
            EntityPredicate.CODEC.optionalFieldOf("condition", EntityPredicate.Builder.create().build()).forGetter(MotionParticlesEnchantmentEffect::condition),
        ).apply(it, ::MotionParticlesEnchantmentEffect) }
    }
}