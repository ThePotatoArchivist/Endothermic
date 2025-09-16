package archives.tater.endothermic.enchantment.entityeffect

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.enchantment.EnchantmentEffectContext
import net.minecraft.enchantment.EnchantmentLevelBasedValue
import net.minecraft.enchantment.effect.EnchantmentEntityEffect
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

data class AddVelocityEnchantmentEntityEffect(val multiplier: EnchantmentLevelBasedValue) : EnchantmentEntityEffect {
    override fun apply(
        world: ServerWorld,
        level: Int,
        context: EnchantmentEffectContext,
        user: Entity,
        pos: Vec3d
    ) {
        val owner = context.owner ?: return
        user.addVelocity(owner.movement.multiply(multiplier.getValue(level).toDouble()))
        user.velocityModified = true
    }

    override fun getCodec(): MapCodec<AddVelocityEnchantmentEntityEffect> = CODEC

    companion object {
        val CODEC: MapCodec<AddVelocityEnchantmentEntityEffect> = RecordCodecBuilder.mapCodec { it.group(
            EnchantmentLevelBasedValue.CODEC.fieldOf("multiplier").forGetter(AddVelocityEnchantmentEntityEffect::multiplier)
        ).apply(it, ::AddVelocityEnchantmentEntityEffect) }
    }
}