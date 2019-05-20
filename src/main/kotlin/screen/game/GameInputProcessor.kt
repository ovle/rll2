package screen.game

import com.badlogic.gdx.Input
import model.game.entity.action.ActionType
import screen.BaseInputProcessor
import utils.Direction


class GameInputProcessor(val delegate: GameInputProcessorDelegate) : BaseInputProcessor() {

    private val movePlayerKeys = setOf(Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.W)

    init {
        this
                .handler(EventType.IsKeyPressed, movePlayerKeys
                ) { delta: Float, code: Int ->
                    val direction = when (code) {
                        Input.Keys.A -> Direction.Left
                        Input.Keys.D -> Direction.Right
                        Input.Keys.W -> Direction.Up
                        Input.Keys.S -> Direction.Down
                        else -> throw UnsupportedOperationException("not supported key code $code")
                    }

                    delegate.onMoveKeyPressed(direction, delta)
                }
                .handler(EventType.KeyDown, movePlayerKeys
                ) { delta: Float, code: Int ->
                    delegate.onMoveKeyDown()
                }
                .handler(EventType.KeyUp, movePlayerKeys
                ) { delta: Float, code: Int ->
                    delegate.onMoveKeyUp()
                }
                .handler(EventType.KeyDown, setOf(Input.Keys.Z)
                ) { delta: Float, code: Int ->
                    delegate.onActionKeyDown(ActionType.StabHit)
                }
                .handler(EventType.KeyDown, setOf(Input.Keys.X)
                ) { delta: Float, code: Int ->
                    delegate.onActionKeyDown(ActionType.SlashHit)
                }
                .handler(EventType.KeyDown, setOf(Input.Keys.C)
                ) { delta: Float, code: Int ->
                    delegate.onActionKeyDown(ActionType.Block)
                }
                .handler(EventType.KeyDown, setOf(Input.Keys.SPACE)
                ) { delta: Float, code: Int ->
                    delegate.onActionKeyDown(ActionType.Invoke)
                }
                .handler(EventType.KeyDown, setOf(Input.Keys.E)
                ) { delta: Float, code: Int ->
                    delegate.onActionKeyDown(ActionType.Common)
                }
                .handler(EventType.KeyDown, setOf(Input.Keys.ESCAPE)
                ) { delta: Float, code: Int ->
                    delegate.onActionKeyDown(ActionType.Die)
                }
                .handler(EventType.MouseKeyDown, setOf(Input.Buttons.LEFT)
                ) { delta: Float, code: Int ->
                    delegate.onMouseLeftButtonDown()
                }
                .handler(EventType.MouseKeyDown, setOf(Input.Buttons.RIGHT)
                ) { delta: Float, code: Int ->
                    delegate.onMouseRightButtonDown()
                }
                .mouseMoveHandler { screenX: Int, screenY: Int ->
                    delegate.onMouseMoved(screenX, screenY)
                }

    }
}