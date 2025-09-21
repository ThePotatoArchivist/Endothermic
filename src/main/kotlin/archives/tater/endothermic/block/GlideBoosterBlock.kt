package archives.tater.endothermic.block

import archives.tater.endothermic.util.isOf
import archives.tater.endothermic.util.sealedMapOf
import archives.tater.endothermic.util.set
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.PillarBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityCollisionHandler
import net.minecraft.entity.LivingEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Direction.Axis
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView
import net.minecraft.world.tick.ScheduledTickView
import kotlin.math.abs
import kotlin.math.sign

class GlideBoosterBlock(settings: Settings) : PillarBlock(settings) {
    init {
        defaultState = stateManager.defaultState
            .with(ON_COOLDOWN, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(ON_COOLDOWN)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape = OUTLINES[state[AXIS]]

    override fun onEntityCollision(
        state: BlockState,
        world: World,
        pos: BlockPos,
        entity: Entity,
        handler: EntityCollisionHandler
    ) {
        if (entity !is LivingEntity || !entity.isGliding || state[ON_COOLDOWN]) return

        val axis = state[AXIS]

        val axisVelocity = entity.velocity.getComponentAlongAxis(axis)

        val direction = if (abs(axisVelocity) <= MAX_VELOCITY_LOOKING)
            Direction.getLookDirectionForAxis(entity, axis).direction.offset().toDouble()
        else
            sign(axisVelocity)

        entity.velocity = entity.velocity.withAxis(axis, direction * (abs(axisVelocity) * VELOCITY_MULTIPLIER).coerceAtLeast(MIN_VELOCITY))
        entity.playSound(SoundEvents.ENTITY_BREEZE_WIND_BURST.value())

        world.setBlockState(pos, state.with(ON_COOLDOWN, true))
        world.scheduleBlockTick(pos, this, COOLDOWN)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        world: WorldView?,
        tickView: ScheduledTickView,
        pos: BlockPos?,
        direction: Direction,
        neighborPos: BlockPos?,
        neighborState: BlockState,
        random: Random?
    ): BlockState = if (!state[ON_COOLDOWN] && neighborState isOf this && state[AXIS] == neighborState[AXIS] && direction.axis != state[AXIS] && neighborState[ON_COOLDOWN]) {
        tickView.scheduleBlockTick(pos, this, COOLDOWN)
        state.with(ON_COOLDOWN, true)
    } else state

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        world[pos] = state.with(ON_COOLDOWN, false)
    }

    companion object {
        val AXIS: EnumProperty<Axis> = PillarBlock.AXIS
        val ON_COOLDOWN: BooleanProperty = BooleanProperty.of("on_cooldown")

        val OUTLINES = sealedMapOf<Axis, VoxelShape> { when (it) {
            Axis.X -> createCuboidShape(7.0, 0.0, 0.0, 9.0, 16.0, 16.0)
            Axis.Y -> createCuboidShape(0.0, 7.0, 0.0, 16.0, 9.0, 16.0)
            Axis.Z -> createCuboidShape(0.0, 0.0, 7.0, 16.0, 16.0, 9.0)
        } }

        const val MIN_VELOCITY = 2.0
        const val VELOCITY_MULTIPLIER = 1.5
        const val MAX_VELOCITY_LOOKING = MIN_VELOCITY / VELOCITY_MULTIPLIER
        const val COOLDOWN = 4
    }
}
