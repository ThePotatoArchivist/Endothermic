package archives.tater.endothermic.datagen

import archives.tater.endothermic.registry.EndothermicDamageTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.DamageTypeTags
import java.util.concurrent.CompletableFuture

class DamageTypeTagGenerator(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricTagProvider<DamageType>(output, RegistryKeys.DAMAGE_TYPE, registriesFuture) {

    override fun configure(wrapperLookup: RegistryWrapper.WrapperLookup) {
        with (builder(DamageTypeTags.IS_PLAYER_ATTACK)) {
            add(EndothermicDamageTypes.DASH_ATTACK)
        }
        with (builder(DamageTypeTags.IS_PROJECTILE)) {
            add(EndothermicDamageTypes.DASH_ATTACK)
        }
        with (builder(DamageTypeTags.NO_KNOCKBACK)) {
            add(EndothermicDamageTypes.DASH_ATTACK)
        }
        with(builder(EndothermicDamageTypes.BYPASSES_DASH)) {
            forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
            add(EndothermicDamageTypes.DASH_ATTACK)
            add(DamageTypes.FLY_INTO_WALL)
            add(DamageTypes.FALL)
        }
    }
}