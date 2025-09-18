package archives.tater.endothermic.entity

import archives.tater.endothermic.registry.EndothermicEntities
import archives.tater.endothermic.registry.EndothermicTrackedDataHandlers
import archives.tater.endothermic.util.getValue
import archives.tater.endothermic.util.registerTrackedData
import archives.tater.endothermic.util.setValue
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LazyEntityReference
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


class FloatingDragonEggEntity(type: EntityType<out FloatingDragonEggEntity>, world: World) : Entity(type, world) {
    private var animationTicks by ANIMATION_TICKS

    var crystals by CRYSTALS
        private set

    constructor(world: World, pos: BlockPos, crystals: List<EndCrystalEntity>) : this(EndothermicEntities.FLOATING_DRAGON_EGG, world) {
        setPosition(pos.toBottomCenterPos())
        world.removeBlock(pos, false)
        this.crystals = crystals.map { LazyEntityReference(it.uuid) }
    }

    override fun initDataTracker(builder: DataTracker.Builder) {
        builder.add(CRYSTALS, listOf())
        builder.add(ANIMATION_TICKS, 0)
    }

    override fun tick() {
        if (animationTicks < ASCEND_TIME) {
            setPos(x, y + ASCEND_RATE, z)
            animationTicks++
        }
    }

    override fun damage(
        world: ServerWorld?,
        source: DamageSource?,
        amount: Float
    ): Boolean = false

    override fun readCustomData(view: ReadView) {
        crystals = view.read(CRYSTAL_POSITIONS_DATA, CRYSTALS_CODEC).orElseGet(::listOf)
        animationTicks = view.getInt(ANIMATION_TICKS_DATA, 0)
    }

    override fun writeCustomData(view: WriteView) {
        view.put(CRYSTAL_POSITIONS_DATA, CRYSTALS_CODEC, crystals)
        view.putInt(ANIMATION_TICKS_DATA, animationTicks)
    }

    companion object {
        private val CRYSTALS = registerTrackedData<FloatingDragonEggEntity, _>(EndothermicTrackedDataHandlers.END_CRYSTAL_LIST)

        private val CRYSTALS_CODEC = LazyEntityReference.createCodec<EndCrystalEntity>().listOf()

        private const val CRYSTAL_POSITIONS_DATA = "crystal_positions"

        private val ANIMATION_TICKS = registerTrackedData<FloatingDragonEggEntity, _>(TrackedDataHandlerRegistry.INTEGER)

        const val ASCEND_TIME = 200
        private const val ASCEND_RATE = 0.08

        private const val ANIMATION_TICKS_DATA = "animation_ticks"
    }
}