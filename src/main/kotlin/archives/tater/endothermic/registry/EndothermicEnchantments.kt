package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.enchantment.effect.MotionParticlesEnchantmentEffect
import archives.tater.endothermic.enchantment.effect.VelocityScaledDamageEnchantmentEffect
import archives.tater.endothermic.enchantment.entityeffect.AddVelocityEnchantmentEntityEffect
import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.enchantment.effect.EnchantmentEffectEntry
import net.minecraft.enchantment.effect.EnchantmentEntityEffect
import net.minecraft.enchantment.effect.EnchantmentValueEffect
import net.minecraft.enchantment.effect.TargetedEnchantmentEffect
import net.minecraft.entity.damage.DamageType
import net.minecraft.loot.context.LootContextTypes
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.context.ContextType

object EndothermicEnchantments {
    fun <T> register(id: Identifier, type: ComponentType<T>): ComponentType<T> =
        Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, id, type)

    fun <T> register(id: Identifier, codec: Codec<T>): ComponentType<T> = register(id, ComponentType.builder<T>().codec(codec).build())

    fun <T> register(path: String, codec: Codec<T>): ComponentType<T> = register(Endothermic.id(path), codec)

    fun <T> registerEntryList(path: String, effect: Codec<T>, contextType: ContextType): ComponentType<List<EnchantmentEffectEntry<T>>> =
        register(path, EnchantmentEffectEntry.createCodec(effect, contextType).listOf())

    @JvmField
    val VELOCITY_SCALED_DAMAGE = registerEntryList(
        "velocity_scaled_damage",
        VelocityScaledDamageEnchantmentEffect.CODEC,
        LootContextTypes.ENCHANTED_DAMAGE
    )

    @JvmField
    val REPLACE_DAMAGE_TYPE = registerEntryList(
        "replace_damage_type",
        DamageType.ENTRY_CODEC,
        LootContextTypes.ENCHANTED_DAMAGE
    )

    @JvmField
    val MOTION_PARTICLES = register("motion_particles", MotionParticlesEnchantmentEffect.CODEC)

    @JvmField
    val DAMAGE_TAKEN = registerEntryList(
        "damage_taken",
        EnchantmentValueEffect.CODEC,
        LootContextTypes.ENCHANTED_DAMAGE
    )

    /**
     * Runs for all equipment on both entities involved, unlike `minecraft:post_attack` which for attacker only runs on weapon
     */
    @JvmField
    val POST_ATTACK = register<List<TargetedEnchantmentEffect<EnchantmentEntityEffect>>>("post_attack",
        TargetedEnchantmentEffect.createPostAttackCodec(EnchantmentEntityEffect.CODEC, LootContextTypes.ENCHANTED_DAMAGE).listOf()
    )

    fun init() {
        Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Endothermic.id("add_velocity"), AddVelocityEnchantmentEntityEffect.CODEC)
    }
}
