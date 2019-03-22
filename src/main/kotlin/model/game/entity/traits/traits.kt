package model.game.entity.traits

import com.badlogic.gdx.math.Rectangle
import model.game.entity.action.Action
import utils.geom.Point


interface Movable {

    /**
     * position in terms of model
     */
    var position: Point;

    fun move(dx: Double, dy: Double, checkAllowMove:(Point) -> Boolean = {true}): Boolean {
        val newPosition = position.translate(dx, dy)
        return if (checkAllowMove.invoke(newPosition)) {
            position = newPosition
            true
        } else {
            false
        }
    }
}


interface Displayable {

    val id: Long

    /**
     * position in terms of model
     */
    val position: Point
}

interface CollisionSubject {

    /**
     * position in terms of model
     */
    val position: Point

    /**
     * collision box in terms of model
     */
    val box:Rectangle
}


interface ActionTarget {
    val name: String
}

interface ActionOwner {
    val currentTargets: MutableCollection<ActionTarget>
    val currentAction: Action?

    fun addTargets(targets: Collection<ActionTarget>) {
        currentTargets.addAll(targets)
    }

    fun removeTargets(targets: Collection<ActionTarget>) {
        currentTargets.removeAll(targets)
    }
}

class ActionHelper: ActionOwner {
    override val currentAction: Action? = null
    override val currentTargets = mutableListOf<ActionTarget>()
}