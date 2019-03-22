package model.game

import model.game.entity.Entity
import utils.Direction


fun moveEntity(entity: Entity, direction: Direction, delta: Float, gameState: GameState) {
    val step = delta * entity.template.moving!!.speed
    val dx = direction.dx(step)
    val dy = direction.dy(step)

    if (dx == 0.0 && dy == 0.0) return

    val result = entity.move(dx, dy, { !collideWithAny(entity, it, gameState) })
    if (result) {
//        checkAreas(result)

        if (isPlayer(entity, gameState)) {
            checkChunksToLoad(dx, dy, entity, gameState)
            checkChunksToUnload(gameState)
        }

        entityMoved(entity, dx, dy)
    }
}


