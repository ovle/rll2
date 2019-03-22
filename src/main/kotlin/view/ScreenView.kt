package view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import launcher.AppConfig
import launcher.GameSession

/**
 * Base libgdx-wrap view using for screen
 */
abstract class ScreenView() {

    val skinPath = "resources/skins/rll/uiskin.json"

    lateinit var guiStage: Stage //uses it own batch/camera

    //todo lazy
    var camera: OrthographicCamera = OrthographicCamera()
    val spriteBatch: SpriteBatch = SpriteBatch()

    lateinit protected var skin: Skin

    protected open val needShowBackButton: Boolean = true


    fun init(): ScreenView {
        guiStage = Stage(FitViewport(AppConfig.width.toFloat(), AppConfig.height.toFloat()))

        skin = Skin(Gdx.files.internal(skinPath))

//        Gdx.input.setCursorImage(Pixmap(FileHandle("rll-client/assets/skins/rll/cursor.png")), 4, 4);

        if (needShowBackButton) {
            addBackButton()
        }
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        camera.setToOrtho(false, w, h)
        camera.update()

        initIntr()

        return this
    }

    open protected fun initIntr() {}


    private fun addBackButton() {
        val btn = TextButton("Back", skin)

        val btnWidth = DEFAULT_BTN_SIZE.width.toFloat()
        val btnHeight = DEFAULT_BTN_SIZE.height.toFloat()
        btn.setSize(btnWidth, btnHeight)
        btn.setPosition(10.0f, Gdx.graphics.height - btnHeight - 10)

        btn.addListener(object: ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                GameSession.back()
            }
        })
        addActor(btn)
    }


    fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        renderIntr(delta)

        spriteBatch.begin()
        renderSpritesIntr(delta, spriteBatch)
        spriteBatch.end()

        guiStage.act(delta)
        guiStage.draw()
    }

    open protected fun renderIntr(delta: Float){}
    open protected fun renderSpritesIntr(delta: Float, batch: SpriteBatch?){}


    fun resize(width: Int, height: Int) {
        guiStage.viewport.update(width, height)
    }

    protected fun addActor(actor: Actor): ScreenView {
        guiStage.addActor(actor)
        return this
    }


    fun dispose() {
        disposeIntr()

        spriteBatch.dispose()

        skin.dispose()
        guiStage.dispose()
    }

    open protected fun disposeIntr() {}
}