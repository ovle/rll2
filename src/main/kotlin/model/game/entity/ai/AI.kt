package model.game.entity.ai

import model.game.*
import model.game.entity.Entity
import utils.Direction


interface AI {
    fun update(delta: Float, owner: Entity, gameState: GameState)
}

class BaseAI: AI {

    fun target(gameState: GameState) = gameState.player.position

    var moving = false

    //todo
    override fun update(delta: Float, owner: Entity, gameState: GameState) {
        val target = target(gameState)

        fun success(owner: Entity) = target.distance(owner.position) < 2.0f

        if (moving) {
            if (success(owner)) {
                moving = false
                entityFinishMove(owner)
            }

        } else {
            if (!success(owner)) {
                moving = true
                entityStartMove(owner)
            }
        }

        if (moving) {
            val (dx, dy) = target - owner.position
            val dxAbs = Math.abs(dx)
            val dyAbs = Math.abs(dy)
            val direction = if (dxAbs > dyAbs) {
                if (dx > 0) Direction.Right else Direction.Left
            } else {
                if (dy > 0) Direction.Up else Direction.Down
            }

            moveEntity(owner, direction, delta, gameState)
        }
    }

}