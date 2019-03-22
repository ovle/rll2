package view.imp.game.sprite

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.entity.Entity
import util.Registry
import util.plus
import util.random
import util.times
import view.GLOBAL_SCALE
import view.config.SpriteConfig
import view.config.SpritePartConfig
import view.config.SpritesheetConfig
import view.imp.game.sprite.impl.CompositeSprite
import view.imp.game.sprite.impl.Sprite
import view.imp.game.texture.GAME_ITEMS_TEXTURE
import view.imp.game.texture.TextureRegistry
import java.awt.Dimension
import java.awt.Point


interface SpriteFactory {
    fun sprite(texture: Texture, sheetId: String, entity: Entity): Renderable
}

class BaseSpriteFactory() : SpriteFactory {

    private val spriteConfigRegistry = kodein.instance<Registry<String, SpritesheetConfig>>()
    private val textureRegistry = kodein.instance<TextureRegistry>()

    override fun sprite(texture: Texture, sheetId: String, entity: Entity): Renderable {
        val parts = mutableListOf<Renderable>()
        parts += parts(entity.template.id, sheetId, texture)

        //todo
        if (entity.template.id == "human") {
            val items = entity.inventory.items
            val itemsTexture = textureRegistry[GAME_ITEMS_TEXTURE]

            parts += items.flatMap { parts(it.template.id, "items", itemsTexture) }
        }

        return CompositeSprite(*parts.toTypedArray())
    }

    private fun parts(id: String, sheetId: String, texture: Texture): List<Sprite> {
        val scale = GLOBAL_SCALE.toInt()

        val config = spriteConfigRegistry[sheetId]!!
        val spriteVariant = config.sprites!!.filter { it.id == id }.random()
        val regionWidth = config.regionSize?.width ?: spriteVariant!!.size!!.width

        val parts = (config.parts!!).map {
            return@map partSprite(it, regionWidth, scale, spriteVariant, texture)
        }
        return parts
    }

    private fun partSprite(it: SpritePartConfig, regionWidth: Int, scale: Int, spriteVariant: SpriteConfig?, texture: Texture): Sprite {
        val regionHeight = it.height ?: spriteVariant!!.size!!.height
        val regionX = it.indexX * regionWidth + (spriteVariant?.origin?.x ?: 0)
        val regionY = it.Y + (spriteVariant?.origin?.y ?: 0) + regionHeight * (spriteVariant?.index ?: 0)
        val region = TextureRegion(texture, regionX, regionY, regionWidth, regionHeight)

        val animations = it.animations?.map {
            FrameAnimation(
                    id = it.id,
                    texture = texture,
                    frameSize = Dimension(regionWidth, regionHeight),
                    start = Point(it.startIndexX * regionWidth, it.startIndexY * regionHeight + region.regionY),
                    steps = it.steps,
                    frameDuration = it.frameDuration,
                    repeat = it.repeat,
                    terminal = it.terminal
            )
        }

        val spriteVariantOffset = spriteVariant?.offset ?: Point(0, 0)
        val offset = (spriteVariantOffset + it.offset) * scale
        return Sprite(
                id = it.id,
                size = Dimension(regionWidth, regionHeight) * scale,
                region = region,
                offset = offset,
                animations = animations?.associate { Pair(it.id, it) }
        )
    }

    //    fun sprite(textureArea: Rectangle, tileSize: Dimension, scale: Int, texture: Texture?, offset: Point = Point(0, 0)): Sprite {
    //        val width = textureArea.width * tileSize.width
    //        val height = textureArea.height * tileSize.height
    //        val x = textureArea.x * tileSize.width
    //        val y = textureArea.y * tileSize.height
    //
    //        val textureRegion = TextureRegion(texture, x, y, width, height) //rev x-y
    //        return Sprite(
    //                "main",
    //                Dimension(width * scale, height * scale),
    //                region = textureRegion,
    //                offset = offset
    //        )
    //    }
}
