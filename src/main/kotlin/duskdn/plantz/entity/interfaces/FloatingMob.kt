package duskdn.plantz.entity.interfaces

import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

interface FloatingMob {

    var checkedSpawn: Boolean

    var spawnPos: Vec3?

    var isFloating: Boolean

    var flyingNavigation: PathNavigation

}