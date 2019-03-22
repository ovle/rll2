package screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.files.FileHandle
import view.ScreenView
import java.util.*

/**
 * Base screen with libgdx scene building routine
 */
abstract class BaseScreen : Screen {

    lateinit var view: ScreenView
    private val input: InputMultiplexer = InputMultiplexer()

    open val musicPath: String? = null
    private var music: Music? = null


    override fun show() {
        beforeInitView()
        view = initView().init()
        afterInitView()

        initInputProcessors()
        Gdx.input.inputProcessor = input

        if (musicPath != null) {
            music = Gdx.audio.newMusic(FileHandle(musicPath))
            music?.isLooping = true
            music?.play()
        }
    }


    /**
     * returns this controller's view.
     * invoked once during initialization
     */
    protected abstract fun initView(): ScreenView

    /**
     * extention point for specific initialization
     */
    protected open fun beforeInitView() {}
    protected open fun afterInitView() {}


    private fun initInputProcessors() {
        val processors = getInputProcessors()
        processors.forEach({ input.addProcessor(it)})
    }

    private fun getInputProcessors(): Iterable<InputProcessor> {
        val result = LinkedList<InputProcessor>()
        result.add(view.guiStage)
        result.addAll(getInputProcessorsIntr())
        return result
    }

    /**
     * returns inputs list
     * invoked once during initialization
     */
    protected open fun getInputProcessorsIntr(): Collection<InputProcessor> {
        return mutableListOf()
    }

    override fun render(delta: Float) {
        view.render(delta)
        input.processors.forEach {
            when (it) {
                is BaseInputProcessor -> it.tick(delta)
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        view.resize(width, height)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
        music?.stop()
    }

    override fun dispose() {
        view.dispose()
    }
}