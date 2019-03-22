package utils

/**
 * Possible 2d moving direction
 */
public enum class Direction {
    Left,
    Right,
    Up,
    Down;

    public fun opposite(): Direction {
        return when(this) {
            Left -> Right
            Right -> Left
            Up -> Down
            Down -> Up
        }
    }

    fun dx(step: Double): Double {
        return when(this) {
            Left -> -step
            Right -> step
            else -> 0.0
        }
    }
    fun dy(step: Double): Double {
        return when(this) {
            Down -> -step
            Up -> step
            else -> 0.0
        }
    }
}
