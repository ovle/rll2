package model.game.entity

import com.badlogic.gdx.math.Rectangle
import model.game.GameState
import model.game.entity.ai.AI
import model.game.entity.item.Inventory
import model.game.entity.traits.*
import model.game.template.EntityTemplate
import utils.geom.Point

/**
 * Basic game object
 * (anything that can be positioned on map, not neccessary have visual representation)
 */
class Entity(
        override val id: Long,
        val template: EntityTemplate,

        val actionHelper: ActionHelper,
        val inventory: Inventory,
        val ai: AI? = null
) : Movable, Displayable, CollisionSubject, ActionOwner by actionHelper, ActionTarget {

    override var position: Point
        get() = Point(box.x.toDouble(), box.y.toDouble())
        set(value) {
            box.x = value.x.toFloat()
            box.y = value.y.toFloat()
        }

    override val box: Rectangle = Rectangle(0f, 0f, template.box.width.toFloat(), template.box.height.toFloat())

    override val name: String
        get() = template.name

    fun update(delta: Float, gameState: GameState) {
        ai?.update(delta, this, gameState)
    }
}
