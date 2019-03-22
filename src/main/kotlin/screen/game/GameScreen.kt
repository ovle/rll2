package screen.game

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.FPSLogger
import com.github.salomonbrys.kodein.instance
import launcher.gameState
import launcher.kodein
import model.game.*
import model.game.chunk.ChunkRegistry
import model.game.entity.action.Action
import model.game.entity.action.ActionType
import screen.BaseScreen
import utils.Direction
import view.ScreenView
import view.imp.game.GameView
import java.awt.Point


class GameScreen : BaseScreen(), GameInputProcessorDelegate {

    private val fpsLog = FPSLogger()

    val gameState: GameState = gameState()
    val inputProcessor = GameInputProcessor(this)
    lateinit var gameView: GameView

    private val cursorPosition: Point = Point(0, 0)


    override fun afterInitView() {
        init(gameState)
    }

    override fun initView(): ScreenView {
        gameView = GameView(gameState)
        return gameView
    }

    override fun getInputProcessorsIntr(): Collection<InputProcessor> {
        return mutableListOf(inputProcessor)
    }

    override fun render(delta: Float) {
        super.render(delta)

        update(gameState, delta)
//        fpsLog.log()
    }

    override fun hide() {
        super.hide()
        kodein.instance<ChunkRegistry>().clear()
    }

    private fun getActiveEntity() = gameState.player

    //------------------------------------------------------------------
    //  GameInputProcessorDelegate
    //------------------------------------------------------------------

    override fun onMoveKeyDown() =  entityStartMove(getActiveEntity())

    override fun onMoveKeyUp() = entityFinishMove(getActiveEntity())

    override fun onMoveKeyPressed(to: Direction, delta: Float) {
        moveEntity(getActiveEntity(), to, delta, gameState)
    }

    override fun onActionKeyDown(actionType: ActionType) = entityDidAction(getActiveEntity(), Action(actionType))


    override fun onMouseLeftButtonDown() {
        val gamePoint = gameView.toGamePoint(cursorPosition)
        System.out.println("$gamePoint")

        //game.toggleSelection(getActiveEntity(), gamePoint)
//        gameView.onClickAtEntities(entities)
    }

    override fun onMouseRightButtonDown() { }

    override fun onMouseMoved(screenX: Int, screenY: Int) {
//        System.out.println("$screenX, $screenY")
        cursorPosition.x = screenX
        cursorPosition.y = screenY
    }
}