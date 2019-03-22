package utils.geom

//import model.game.Constants
import java.io.Serializable
import java.math.BigDecimal

/**
 * in-game coordinates of any object. usable for model only (views use java.awt.Point)
 * note, that this class is mutable for now
 * copy constructor {@link #from(Point)} can still be used when appropriate
 */
data class Point(
        var x: Double,
        var y: Double
) : Serializable {

    companion object {
        fun from(otherPoint: Point): Point {
            return Point(otherPoint.x, otherPoint.y)
        }
    }


    constructor(point: Point) : this(point.x, point.y)


    fun translate(dx: Double = 0.0, dy: Double = 0.0): Point {
        return Point(x = x + dx, y = y + dy)
    }

    fun distance(from: Point): Double {
        val fromX = from.x - x
        val fromY = from.y - y

        return Math.sqrt(fromX * fromX + fromY * fromY)
    }

    fun isNear(from: Point, precision: Double): Boolean {
        return distance(from) <= precision
    }
//
//    public fun toGridPoint(): java.awt.Point {
//        val floor = Math::floor
//        return java.awt.Point(floor(x).toInt(), floor(y).toInt())
//    }
//
//    //todo hack
//    public fun aligned(): Point {
//        val alignedValue = {
//            value: Double -> BigDecimal.valueOf(value)
//                .setScale(2, BigDecimal.ROUND_DOWN)
//                .toDouble()
//        }
//
//        return Point(alignedValue(x), alignedValue(y))
//    }

    operator fun plus(point: Point): Point {
        return Point(x = x + point.x, y = y + point.y)
    }

    operator fun minus(point: Point): Point {
        return Point(x = x - point.x, y = y - point.y)
    }


    override fun toString(): String {
        return "Point{x=$x, y=$y}"
    }

    //todo
//    fun centerInTile(): Point {
//        x -= TILE_S.width / 2
//        y += Constants.TILE_SIZE.height / 2
//        return this
//    }
}
