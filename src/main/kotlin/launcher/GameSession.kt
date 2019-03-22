package launcher

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import model.Player
import model.WORLD_SIZE
import model.World
import screen.mainMenu.MainMenuScreen
import java.util.*


object GameSession : Game() {

    val player = Player()
    val world = World(WORLD_SIZE)

    val screens = Stack<Screen>()

    override fun create() {
        show(MainMenuScreen())
    }

    fun show(screen: Screen) {
        val oldScreen = if (screens.isNotEmpty()) screens.peek() else null
        oldScreen?.hide()

        screens.push(screen)
        setScreen(screen)
    }

    fun back() {
        if (screens.size <= 1) {
            throw IllegalStateException("no screens for return to")
        }

        val oldScreen = screens.pop()
        show(screens.peek())
        oldScreen.dispose()
    }
}