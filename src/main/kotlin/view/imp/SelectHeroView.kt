package view.imp

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.salomonbrys.kodein.instance
import launcher.GameSession
import launcher.kodein
import utils.geom.Point
import view.GLOBAL_SCALE
import view.ScreenView
import view.WALK_ANIMATION_NAME
import view.imp.game.sprite.Renderable
import view.imp.game.sprite.SpriteFactory
import view.imp.game.texture.GAME_CHARS_PORTRAIT_TEXTURE
import view.imp.game.texture.GAME_CHARS_TEXTURE
import view.imp.game.texture.TextureRegistry
import java.awt.Dimension


interface SelectHeroViewDelegate {
    fun onCharSelected(charId: String)
}

class SelectHeroView(
        private val delegate: SelectHeroViewDelegate
) : ScreenView() {

    private val spriteFactory = kodein.instance<SpriteFactory>()

    private val textureRegistry = kodein.instance<TextureRegistry>()

    private val PORTRAIT_TEXTURE_REGION = Dimension(24, 24)

    private val CHAR_KEYS = arrayOf(
            arrayOf("rogue", "arcaneWizard", "southWarrior"),
            arrayOf("lightPriest", null, "darkWizard"),
            arrayOf("northWarrior", "southWizard", "knight")
    )

    private lateinit var playerSprite: Renderable


    override fun initIntr() {
        super.initIntr()

//        Sprites.init()
//        initPlayerSprite(currentCharId())
        initPortraits()
    }

    private fun initPortraits() {
        val texture = textureRegistry[GAME_CHARS_PORTRAIT_TEXTURE]
        val count = 3

        val width = PORTRAIT_TEXTURE_REGION.width
        val height = PORTRAIT_TEXTURE_REGION.height

        for (x in (0..count - 1)){
            for (y in (0..count - 1)) {
                if (CHAR_KEYS[y][x] == null) continue

                val region = TextureRegion(
                        texture,
                        width * x, width * y,
                        width, height
                )
                addImageButton(region, x, y, width, height)
            }
        }
    }

    private fun addImageButton(region: TextureRegion, x: Int, y: Int, width: Int, height: Int) {
        val button = ImageButton(TextureRegionDrawable(region))
        val centerPoint = centerPoint()
        val scale = GLOBAL_SCALE

        val positionX = centerPoint.x - (x - 0.5) * PORTRAIT_TEXTURE_REGION.width * scale
        val positionY = centerPoint.y - (y - 0.5) * PORTRAIT_TEXTURE_REGION.height * scale
        button.setPosition(positionX.toFloat(), positionY.toFloat())
        button.setSize(width * scale, height * scale)
        button.imageCell.expand().fill()

        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, clickX: Float, clickY: Float) {
                onCharSelected(CHAR_KEYS[y][x]!!)
            }
        })

        addActor(button)
    }

    private fun currentCharId() = GameSession.player.charId

    private fun onCharSelected(charId: String) {
        delegate.onCharSelected(charId)
//        initPlayerSprite(charId)
    }

    override fun renderSpritesIntr(delta: Float, batch: SpriteBatch?) {
        playerSprite.render(batch!!)
    }

//    private fun initPlayerSprite(charId: String?) {
//        val centerPoint = centerPoint()
//        playerSprite = spriteFactory.sprite(textureRegistry[GAME_CHARS_TEXTURE], "humanoid", charId ?: currentCharId())
//        val scale = GLOBAL_SCALE
//        playerSprite.move(
//            centerPoint - Point(
//                PORTRAIT_TEXTURE_REGION.width * scale.toDouble() / 4,
//                PORTRAIT_TEXTURE_REGION.height * scale.toDouble() / 4
//            )
//        )
//
//        playerSprite.startAnimation(WALK_ANIMATION_NAME)
//    }

    private fun centerPoint(): Point {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        val centerPoint = Point(w / 2.toDouble(), h / 2.toDouble())
        return centerPoint
    }
}