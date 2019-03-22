package view.imp

import view.ScreenView
import view.widget.Menu


class MenuView(private val delegate: MenuViewDelegate) : ScreenView() {

    override val needShowBackButton: Boolean = false

    override fun initIntr() {
        super.initIntr()

        delegate.menuConfig.forEach {
            addActor(Menu(skin, it))
        }
    }
}