package view.imp.game.sprite

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import utils.geom.Point


interface Renderable {

    var flipped: Boolean

    var position: Point
    var z: Float

    var color: Color?

    var opaque: Boolean

    fun render(batch: SpriteBatch)

    //todo
    fun move(point: Point) {
        position = Point(point)
//        position.x = point.x
//        position.y = point.y
    }

    fun startAnimation(id: String)

    fun stopAnimation(id: String)

    fun flip() {
        flipped = !flipped
    }
}