package archives.tater.endothermic.item

import archives.tater.endothermic.registry.EndothermicDataAttachments.DASH_TICKS
import archives.tater.endothermic.util.set
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class DashRocketItem(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        if (!user.isGliding) return ActionResult.PASS

        if (world !is ServerWorld) return ActionResult.SUCCESS

        if (user.detachAllHeldLeashes(null))
            world.playSoundFromEntity(null, user, SoundEvents.ITEM_LEAD_BREAK, SoundCategory.NEUTRAL, 1.0f, 1.0f)

        val stack = user.getStackInHand(hand)

        user[DASH_TICKS] = MAX_DASH_TICKS
        world.playSoundFromEntity(null, user, SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, user.soundCategory, 1f, 1f)
        stack.decrementUnlessCreative(1, user)
        user.incrementStat(Stats.USED.getOrCreateStat(this))

        return ActionResult.SUCCESS
    }

    companion object {
        const val MAX_DASH_TICKS = 40
        const val ATTACK_VELOCITY_COEFFICIENT = 1.25f
        const val WALL_DAMAGE_MULTIPLIER = 2f
        const val VELOCITY_KNOCKBACK_MULTIPLIER = 2.0
    }
}