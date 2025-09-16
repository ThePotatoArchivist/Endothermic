package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.enchantment.effect.MotionParticlesEnchantmentEffect
import archives.tater.endothermic.enchantment.effect.VelocityScaledDamageEnchantmentEffect
import archives.tater.endothermic.enchantment.entityeffect.AddVelocityEnchantmentEntityEffect
import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.enchantment.effect.EnchantmentEffectEntry
import net.minecraft.entity.damage.DamageType
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier

object EndothermicEnchantments {
    fun <T> register(id: Identifier, type: ComponentType<T>): ComponentType<T> =
        Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, id, type)

    fun <T> register(id: Identifier, codec: Codec<T>): ComponentType<T> = register(id, ComponentType.builder<T>().codec(codec).build())

    fun <T> register(path: String, codec: Codec<T>): ComponentType<T> = register(Endothermic.id(path), codec)

    @JvmField
    val VELOCITY_SCALED_DAMAGE = register<List<EnchantmentEffectEntry<VelocityScaledDamageEnchantmentEffect>>>("velocity_scaled_damage", EnchantmentEffectEntry.createCodec(
        VelocityScaledDamageEnchantmentEffect.CODEC,
        LootContextTypes.ENCHANTED_DAMAGE
    ).listOf())

    @JvmField
    val REPLACE_DAMAGE_TYPE = register<List<EnchantmentEffectEntry<RegistryEntry<DamageType>>>>("replace_damage_type",
        EnchantmentEffectEntry.createCodec(
            DamageType.ENTRY_CODEC,
            LootContextTypes.ENCHANTED_DAMAGE
        ).listOf())

    @JvmField
    val MOTION_PARTICLES = register("motion_particles", MotionParticlesEnchantmentEffect.CODEC)

    fun init() {
        Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Endothermic.id("add_velocity"), AddVelocityEnchantmentEntityEffect.CODEC)
    }
}
