package launcher

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import view.COLOR_BG


/**
 * Entry point
 */
fun main(arg: Array<String>) {
    LwjglApplication(GameSession, AppConfig)
}


object AppConfig : LwjglApplicationConfiguration() {
    init {
        initialBackgroundColor = COLOR_BG
        width = 640
        height = 480
        fullscreen = false
    }
}