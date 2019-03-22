package screen.mainMenu

import com.badlogic.gdx.Application
import com.badlogic.gdx.Audio
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio
import com.badlogic.gdx.files.FileHandle
import launcher.GameSession
import screen.BaseScreen
import screen.game.GameScreen
import screen.world.WorldScreen
import screen.selectHero.SelectHeroScreen
import view.ScreenView
import view.imp.MenuView
import view.imp.MenuViewDelegate
import view.widget.Menu


class MainMenuScreen : BaseScreen(), MenuViewDelegate {

    override val musicPath: String?
        get() = "resources/music/song1.mp3"

    override val menuConfig =
            arrayOf(Menu.MenuConfig(
                    "main",
                    arrayOf(
                            Menu.ButtonConfig(
                                    title = "Start game",
                                    onClick = { GameSession.show(GameScreen()) }
                            ),
                            Menu.ButtonConfig(
                                    title = "Worlds",
                                    onClick = { GameSession.show(WorldScreen()) }
                            ),
                            Menu.ButtonConfig(
                                    title = "Cradle",
                                    onClick = { GameSession.show(SelectHeroScreen()) }
                            ),

                            Menu.ButtonConfig(
                                    title = "Exit",
                                    onClick = { Gdx.app.exit() }
                            )
                    )
            )
    )

    override fun initView(): ScreenView {
        return MenuView(this)
    }
}
