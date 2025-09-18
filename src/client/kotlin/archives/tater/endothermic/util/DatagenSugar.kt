package archives.tater.endothermic.util

import net.minecraft.predicate.entity.DamageSourcePredicate
import net.minecraft.predicate.entity.EntityPredicate

fun entityPredicate(init: EntityPredicate.Builder.() -> Unit): EntityPredicate.Builder =
    EntityPredicate.Builder.create().apply(init)

fun damageSourcePredicate(init: DamageSourcePredicate.Builder.() -> Unit): DamageSourcePredicate.Builder =
    DamageSourcePredicate.Builder.create().apply(init)