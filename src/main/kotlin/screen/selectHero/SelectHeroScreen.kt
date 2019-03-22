package screen.selectHero

import screen.BaseScreen
import view.ScreenView
import view.imp.SelectHeroView
import view.imp.SelectHeroViewDelegate

class SelectHeroScreen: BaseScreen(), SelectHeroViewDelegate {

    override fun initView(): ScreenView {
        return SelectHeroView(this)
    }

    override fun onCharSelected(charId: String) {
//        RllGame.playerData.charId = charId
    }
}