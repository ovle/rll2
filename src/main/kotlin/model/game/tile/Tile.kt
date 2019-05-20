package model.game.tile

import java.io.Serializable

/**
 * Basic terrain/chunk part
 */
class Tile (val x: Int, val y: Int, val type: Type): Serializable {

    enum class Type {
        Void,
        Floor,
        Water,
        Solid
    }

    val passable: Boolean
        get() = type != Type.Solid
}
