package archives.tater.endothermic.enchantment.effect

import archives.tater.endothermic.registry.EndothermicParticles
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.enchantment.EnchantmentLevelBasedValue
import net.minecraft.entity.LivingEntity
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.util.math.MathHelper.TAU
import org.joml.Quaternionf
import org.joml.Vector3f

data class MotionParticlesEnchantmentEffect(val count: EnchantmentLevelBasedValue, val condition: EntityPredicate) {
    fun tick(entity: LivingEntity, level: Int) {
        if (!condition.test(null, null, entity)) return // May cause crashes due to missing ServerWorld, it is the responsibility of the enchantment author to not use ServerWorld-dependent entity predicates
        repeat(count.getValue(level).toInt()) {
            val theta = entity.random.nextFloat() * TAU
            val vertical = entity.random.nextFloat() * entity.width * 4
            val radius = entity.random.nextFloat() * entity.width * 1.25f + 0.25f
            val quaternion = Quaternionf().rotateTo(Vector3f(0f, 1f, 0f), entity.movement.toVector3f())
            val offset = Vector3f(radius, vertical, 0f).rotateY(theta).rotate(quaternion)

            entity.world.addParticleClient(
                EndothermicParticles.DASH,
                entity.x + offset.x.toDouble(),
                entity.eyeY + offset.y.toDouble(),
                entity.z + offset.z.toDouble(),
                entity.movement.x,
                entity.movement.y,
                entity.movement.z
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