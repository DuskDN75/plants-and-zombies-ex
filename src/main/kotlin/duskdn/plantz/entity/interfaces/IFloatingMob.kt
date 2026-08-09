package duskdn.plantz.entity.interfaces

import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.phys.Vec3

interface IFloatingMob {

    var checkedSpawn: Boolean

    var spawnPos: Vec3?

    var flyingNavigation: PathNavigation?

}