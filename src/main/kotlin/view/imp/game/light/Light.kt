package view.imp.game.light

import com.badlogic.gdx.graphics.Color
import utils.geom.Point

/**
 * Created by ovle on 01.01.2016.
 */
object Light {

    //todo
    public fun forPosition(position: Point): Color? {

//        if (position.isNear(Point(0.toDouble(), 5.toDouble()), 3.0.toDouble())) return Color(1.0f, 0.4f, 0.4f, 1.0f)

//        return Color(1.0f, 0.7f, 0.7f, 1.0f)
        return Color.WHITE
        //        return if (position.x.toInt() == 1) Color.RED else Color.BLACK
    }
}