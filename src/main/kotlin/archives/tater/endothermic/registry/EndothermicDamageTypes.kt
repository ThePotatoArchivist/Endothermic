package archives.tater.endothermic.registry

import archives.tater.endothermic.Endothermic
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object EndothermicDamageTypes {
    fun of(id: Identifier): RegistryKey<DamageType> = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id)
    fun of(path: String) = of(Endothermic.id(path))

    private fun tagOf(id: Identifier): TagKey<DamageType> = TagKey.of(RegistryKeys.DAMAGE_TYPE, id)
    private fun tagOf(path: String) = tagOf(Endothermic.id(path))

    @JvmField
    val DASH_ATTACK = of("dash_attack")

    @JvmField
    val BYPASSES_DASH = tagOf("bypasses_dash")

    fun init() {}
}