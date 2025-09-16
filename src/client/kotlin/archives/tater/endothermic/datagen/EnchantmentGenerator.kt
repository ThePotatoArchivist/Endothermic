package archives.tater.endothermic.datagen

import archives.tater.endothermic.Endothermic
import archives.tater.endothermic.enchantment.effect.MotionParticlesEnchantmentEffect
import archives.tater.endothermic.enchantment.effect.VelocityScaledDamageEnchantmentEffect
import archives.tater.endothermic.enchantment.entityeffect.AddVelocityEnchantmentEntityEffect
import archives.tater.endothermic.registry.EndothermicDamageTypes
import archives.tater.endothermic.registry.EndothermicEnchantments
import archives.tater.endothermic.registry.EndothermicItems
import archives.tater.endothermic.util.get
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.component.EnchantmentEffectComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentLevelBasedValue.constant
import net.minecraft.enchantment.effect.DamageImmunityEnchantmentEffect
import net.minecraft.enchantment.effect.EnchantmentEffectTarget
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect
import net.minecraft.loot.condition.AllOfLootCondition
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition
import net.minecraft.loot.condition.EntityPropertiesLootCondition
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.TagPredicate
import net.minecraft.predicate.entity.DamageSourcePredicate
import net.minecraft.predicate.entity.EntityFlagsPredicate
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.predicate.entity.MovementPredicate
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntryList
import java.util.concurrent.CompletableFuture

class EnchantmentGenerator(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun configure(
        registries: RegistryWrapper.WrapperLookup,
        entries: Entries
    ) {
        entries.add(registries[RegistryKeys.ENCHANTMENT][DASH])
    }

    override fun getName(): String = "Enchantments"

    companion object : RegistryBuilder.BootstrapFunction<Enchantment> {
        val DASH: RegistryKey<Enchantment> = RegistryKey.of(RegistryKeys.ENCHANTMENT, Endothermic.id("dash"))

        @Suppress("DEPRECATION")
        override fun run(registerable: Registerable<Enchantment>) {
            val damageTypes = registerable.getRegistryLookup(RegistryKeys.DAMAGE_TYPE)

            fun register(key: RegistryKey<Enchantment>, definition: Enchantment.Definition, init: Enchantment.Builder.() -> Unit) {
                registerable.register(key, Enchantment.builder(definition).apply(init).build(key.value))
            }

            val speedPredicate = EntityPredicate.Builder.create().apply {
                flags(EntityFlagsPredicate.Builder.create().flying(true))
                movement(MovementPredicate.speed(NumberRange.DoubleRange.atLeast(24.0)))
            }
            val thisSpeedCondition: LootCondition.Builder = EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, speedPredicate)
            val attackerSpeedCondition = EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, speedPredicate)

            register(DASH, Enchantment.definition(
                RegistryEntryList.of(Registries.ITEM, EndothermicItems.ELYTRA_ENCHANTABLE),
                1,
                1,
                Enchantment.leveledCost(10, 10),
                Enchantment.leveledCost(10, 25),
                8,
                AttributeModifierSlot.CHEST
            )) {
                addEffect(
                    EnchantmentEffectComponentTypes.DAMAGE_IMMUNITY,
                    DamageImmunityEnchantmentEffect.INSTANCE,
                    AllOfLootCondition.builder(
                        thisSpeedCondition,
                        DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().apply {
                            tag(TagPredicate.unexpected(EndothermicDamageTypes.BYPASSES_DASH))
                        })
                    )
                )
                // TODO protection is capped at 0
                addEffect(
                    EnchantmentEffectComponentTypes.DAMAGE_PROTECTION,
                    AddEnchantmentEffect(constant(-25f)),
                    AllOfLootCondition.builder(
                        thisSpeedCondition,
                        DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().apply {
                            tag(TagPredicate.expected(EndothermicDamageTypes.BYPASSES_DASH))
                        })
                    )
                )
                // TODO post attack doesn't check attacker armor
                addEffect(
                    EnchantmentEffectComponentTypes.POST_ATTACK,
                    EnchantmentEffectTarget.ATTACKER,
                    EnchantmentEffectTarget.VICTIM,
                    AddVelocityEnchantmentEntityEffect(constant(2f)),
                    attackerSpeedCondition
                )
                addNonListEffect(
                    EndothermicEnchantments.MOTION_PARTICLES,
                    MotionParticlesEnchantmentEffect(constant(8f), speedPredicate.build())
                )
                // TODO verify
                addEffect(
                    EndothermicEnchantments.REPLACE_DAMAGE_TYPE,
                    damageTypes[EndothermicDamageTypes.DASH_ATTACK],
                    attackerSpeedCondition
                )
                // TODO not working
                addEffect(
                    EndothermicEnchantments.VELOCITY_SCALED_DAMAGE,
                    VelocityScaledDamageEnchantmentEffect(constant(1.25f), minMultiplier = constant(1f)),
                    attackerSpeedCondition
                )
            }
        }
    }
}