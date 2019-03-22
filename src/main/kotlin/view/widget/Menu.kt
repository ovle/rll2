package view.widget

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import launcher.AppConfig
import view.DEFAULT_BTN_SIZE
import java.awt.Point

/**
 * Menu view widget
 */
class Menu(skin: Skin?, config: Menu.MenuConfig) : Group() {

    class MenuConfig(
            val id: String,
            val buttonConfigs: Array<Menu.ButtonConfig>,
            val position: Point? = null
    );

    class ButtonConfig(
            val title: String,
            val onClick: () -> Unit
    );


    private var currPosition = Point(config.position ?: DEFAULT_MENU_POSITION)

    init {
        config.buttonConfigs.forEach {
            buttonConfig ->
                val btn = TextButton(buttonConfig.title, skin)
                btn.setPosition(currPosition.x.toFloat(), currPosition.y.toFloat())
                currPosition.y -= DISTANCE_BETWEEN_BTNS
                btn.setSize(DEFAULT_BTN_SIZE.width.toFloat(), DEFAULT_BTN_SIZE.height.toFloat())
                btn.addListener(object: ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        buttonConfig.onClick.invoke()
                    }
                })
                this.addActor(btn)
        }
    }

    companion object {
        private val DEFAULT_MENU_POSITION = Point(
                (Gdx.graphics.width - DEFAULT_BTN_SIZE.width)/2,
                Gdx.graphics.height - 200
        );
        private val DISTANCE_BETWEEN_BTNS = AppConfig.height / 12
    }
}