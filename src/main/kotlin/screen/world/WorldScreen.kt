package screen.world

import com.badlogic.gdx.InputProcessor
import screen.BaseScreen
import view.ScreenView
import view.imp.WorldView
import view.imp.WorldViewDelegate
import java.awt.Dimension


class WorldScreen: BaseScreen(), WorldViewDelegate {

    companion object {
        private val WORLD_SIZE = Dimension(129, 129)
    }

//    override var worldData: WorldData = WorldData.current ?: WorldData.create(WORLD_SIZE)
    lateinit var worldView: WorldView


    override fun beforeInitView() {}

    override fun initView(): ScreenView {
        worldView = WorldView(this)
        return worldView
    }

    override fun getInputProcessorsIntr(): Collection<InputProcessor> {
        return mutableListOf(WorldInputProcessor(this))
    }

    fun createWorld() {
//        worldData = WorldData.create(WORLD_SIZE)
        worldView.reload()
    }
}