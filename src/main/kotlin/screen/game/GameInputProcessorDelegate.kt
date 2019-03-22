package screen.game

import model.game.entity.action.ActionType
import utils.Direction


interface GameInputProcessorDelegate {

    fun onMoveKeyDown()

    fun onMoveKeyUp()

    fun onMoveKeyPressed(to: Direction, delta: Float)

    fun onActionKeyDown(actionType: ActionType)

    fun onMouseLeftButtonDown()

    fun onMouseRightButtonDown()

    fun onMouseMoved(screenX: Int, screenY: Int)
}