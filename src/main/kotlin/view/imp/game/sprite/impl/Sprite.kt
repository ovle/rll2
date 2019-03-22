package view.imp.game.sprite.impl

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import utils.geom.Point
import view.imp.game.sprite.FrameAnimation
import view.imp.game.sprite.Renderable
import java.awt.Dimension


/**
 * simple sprite for texture region / animation rendering
 */
open class Sprite(
        val id: String,
        val size: Dimension,
        private val offset : java.awt.Point? = null,
        private val region: TextureRegion? = null,
        private val animations: Map<String, FrameAnimation>? = null,
        color: Color? = null
): Renderable {

    override var position: Point = Point(0.0, 0.0)

    override var z: Float = 0.0f

    override var color: Color? = color?.cpy()

    override var opaque: Boolean = false

    override var flipped: Boolean = false

    /**
     * current animation
     */
    private var animation: FrameAnimation? = null


    override fun render(batch: SpriteBatch) {
        animation?.tick()

        val oldColor = batch.color;
        if (color != null) {
            batch.color = color
        }

        val oldAlpha = batch.color.a
        batch.color.a = if (opaque) 0.6f else 1.0f

        batch.draw(
                getDrawable(),
                position.x.toFloat() + (offset?.x ?: 0).toFloat(),
                position.y.toFloat() + (offset?.y ?: 0).toFloat(),
                size.width.toFloat(),
                size.height.toFloat()
        );

        batch.color = oldColor
        batch.color.a = oldAlpha
    }

    private fun getDrawable(): TextureRegion? {
        return animation?.currentFrame ?: region
    }


    override fun startAnimation(id: String) {
        val animationToStart = animations?.get(id) ?: return;
        animation = animationToStart
        animation?.start()
    }

    override fun stopAnimation(id: String) {
        val animationToStop = animations?.get(id) ?: return;

        animationToStop.stop()
        if (animationToStop == animation) {
            animation = null;
        }
    }

    override fun flip() {
        super.flip()

        region?.flip(true, false)
        animations?.values?.forEach { it.flip() }
        if (offset != null) {
            offset.x = -offset.x
        }
    }
}