package view.imp.game.sprite

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import java.awt.Dimension
import java.awt.Point


class FrameAnimation(
        val id: String,
        texture: Texture,
        frameSize: Dimension,
        offset: Point = Point(0, 0),
        frameDuration: Float,
        start: Point,
        steps: Int,
        val repeat: Boolean = false,
        val terminal: Boolean = false
) {

    private var animation: Animation<TextureRegion?>
    private var frames: Array<TextureRegion?>
    private var stateTime: Float = 0.0f
    private var active: Boolean = false

    var currentFrame: TextureRegion? = null
        private set


    init {
        frames = arrayOfNulls<TextureRegion?>(steps)

        var x = start.x;
        var y = start.y;
        (0..steps - 1).forEach {
            frames[it] = TextureRegion(
                    texture,
                    x + offset.x, y + offset.y,
                    frameSize.width, frameSize.height
            )
            x+= frameSize.width
        }

        animation = Animation(frameDuration, *frames)
    }


    fun start() {
        active = true
    }

    fun stop() {
        active = false
        if (!terminal) {
            currentFrame = null
        }
        stateTime = 0.0f
    }


    fun tick() {
        if (!active) {
            return
        }

        stateTime += Gdx.graphics.deltaTime;
        currentFrame = animation.getKeyFrame(stateTime, repeat);

        if (!repeat && animation.isAnimationFinished(stateTime)) {
            stop()
        }
    }

    fun flip() {
        frames.forEach { it?.flip(true, false) }
    }
}