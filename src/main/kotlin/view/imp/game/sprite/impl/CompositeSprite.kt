package view.imp.game.sprite.impl

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import utils.geom.Point
import view.imp.game.sprite.Renderable

/**
 * Composite for Renderables
 */
class CompositeSprite(vararg val children: Renderable): Renderable {

    override var position: Point = Point(0.0, 0.0)

    override var z: Float
        get() = this.children.first().z
        set(value) {
            this.children.forEach { it.z = value }
        }

    override var flipped: Boolean = false

    override var color: Color?
        get() = throw UnsupportedOperationException("this property is write-only")
        set(value) {
            this.children.forEach { it.color = value }
        }

    override var opaque: Boolean
        get() = throw UnsupportedOperationException("this property is write-only")
        set(value) {
            this.children.forEach { it.opaque = value }
        }


    override fun render(batch: SpriteBatch) {
        this.children.forEach { it.render(batch) }
    }

    //todo
    override fun move(point: Point) {
        val dx = point.x - position.x
        val dy = point.y - position.y
        this.children.forEach { it.move(Point(it.position.x + dx, it.position.y + dy)) }

        super.move(point)
    }

    override fun startAnimation(id: String) {
        this.children.forEach { it.startAnimation(id) }
    }

    override fun stopAnimation(id: String) {
        this.children.forEach { it.stopAnimation(id) }
    }

    override fun flip() {
        super.flip()

        this.children.forEach {
            it.flip()
        }
    }
}