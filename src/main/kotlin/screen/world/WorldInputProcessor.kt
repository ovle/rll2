package screen.world

import com.badlogic.gdx.Input
import screen.BaseInputProcessor
import utils.Direction


class WorldInputProcessor(screen: WorldScreen): BaseInputProcessor() {

    private val scrollKeys = setOf(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN)
//    private val movePlayerKeys = setOf(Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.W)

    init {
        this.handler(BaseInputProcessor.EventType.IsKeyPressed, scrollKeys,
                {
                    delta: Float, code: Int ->
                    val direction = when (code) {
                        Input.Keys.LEFT -> Direction.Left
                        Input.Keys.RIGHT -> Direction.Right
                        Input.Keys.UP -> Direction.Up
                        Input.Keys.DOWN -> Direction.Down
                        else -> throw UnsupportedOperationException("not supported key code $code")
                    }

                    screen.worldView.scrollTiles(direction, delta)
                })
        .handler(BaseInputProcessor.EventType.KeyDown, setOf(Input.Keys.R),
                {
                    delta: Float, code: Int -> screen.createWorld()
                })
    }
}