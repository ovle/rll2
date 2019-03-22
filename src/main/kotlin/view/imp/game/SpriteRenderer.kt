package view.imp.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.salomonbrys.kodein.instance
import launcher.gameState
import launcher.kodein
import model.game.*
import model.game.entity.Entity
import model.game.entity.action.actionName
import model.game.entity.traits.Displayable
import model.game.template.EntityTemplate
import utils.geom.Point
import view.PIXELS_PER_TILE
import view.PLAYER_SCREEN_HEIGHT_OFFSET_RATIO
import view.PLAYER_SCREEN_WIDTH_OFFSET_RATIO
import view.WALK_ANIMATION_NAME
import view.imp.game.light.Light
import view.imp.game.sprite.Renderable
import view.imp.game.sprite.SpriteFactory
import view.imp.game.texture.GAME_CHARS_TEXTURE
import view.imp.game.texture.GAME_ENVIRONMENT_TEXTURE_TEMPLATE
import view.imp.game.texture.GAME_ITEMS_TEXTURE
import view.imp.game.texture.TextureRegistry
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList


interface SpriteRendererDelegate {
    val player: Entity
    val entities: Iterable<Displayable>

    val spriteBatch: SpriteBatch

    fun scrollCamera(x: Float, y: Float)
    fun toScreenPoint(position: Point): Point
}


class SpriteRenderer(val delegate: SpriteRendererDelegate) {

    private val spriteFactory = kodein.instance<SpriteFactory>()
    private val textureRegistry = kodein.instance<TextureRegistry>()

    lateinit private var playerSprite: Renderable

    private val sprites = ConcurrentHashMap<Long, Renderable>()
    private val spritesOrderedByZ = CopyOnWriteArrayList<Renderable>()

    init {

        onEntityStartMove {
            val sprite = sprites[it.entity.id]!!;
            sprite.startAnimation(WALK_ANIMATION_NAME)
        }

        onEntityFinishMove {
            val sprite = sprites[it.entity.id]!!;
            sprite.stopAnimation(WALK_ANIMATION_NAME)
        }

        onEntityDidAction {
            val sprite = sprites[it.entity.id]!!;
            sprite.startAnimation(actionName(it.action.type))
        }

        onEntityMoved {
            val entity = it.entity

            if (entity == delegate.player) {
                val dx = it.dx.toFloat() * PIXELS_PER_TILE
                val dy = it.dy.toFloat() * PIXELS_PER_TILE
                delegate.scrollCamera(dx, dy)
            }

            val sprite = sprites[entity.id]!!;
            if (it.dx == 0.0) return@onEntityMoved

            val shouldBeFlipped = (it.dx < 0)
            val needFlip = (shouldBeFlipped != sprite.flipped)
            if (needFlip) {
                sprite.flip()
            }
        }

        onEntitiesLoaded {
            addEntities(it.entities)
        }

        onEntitiesUnloaded {
            removeEntities(it.entities)
        }
    }

    private fun centerAt(player: Entity) {
        val centerSpriteOffset = centerSpriteOffset()
        val dx = -centerSpriteOffset.x.toFloat() + player.position.x.toFloat() * PIXELS_PER_TILE
        val dy = -centerSpriteOffset.y.toFloat() + player.position.y.toFloat() * PIXELS_PER_TILE
        delegate.scrollCamera(dx, dy)

        playerSprite.move(centerSpriteOffset)
    }


    private fun centerSpriteOffset():Point {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        return Point(
                w * PLAYER_SCREEN_WIDTH_OFFSET_RATIO.toDouble(),
                h * PLAYER_SCREEN_HEIGHT_OFFSET_RATIO.toDouble()
        )
    }

    private fun addEntities(entities: Iterable<Entity>) {
        for (entity in entities) {
            addEntity(entity)
        }
    }

    private fun addEntity(entity: Entity) {
        //todo
        val texture = when (entity.template.type) {
            EntityTemplate.Type.Creature -> GAME_CHARS_TEXTURE
            EntityTemplate.Type.Environment -> environmentTextureName(entity.template)
            EntityTemplate.Type.Item -> GAME_ITEMS_TEXTURE
            else -> throw IllegalArgumentException("type not supported")
        }
        val sheetId = when (entity.template.type) {
            EntityTemplate.Type.Creature -> "humanoid"
            EntityTemplate.Type.Environment -> "environment"
            EntityTemplate.Type.Item -> "items"
            else -> throw IllegalArgumentException("type not supported")
        }

        val sprite = spriteFactory.sprite(textureRegistry[texture], sheetId, entity)
            .apply { if (sheetId == "humanoid") z = 1.0f }
        addSprite(sprite, entity.id)

        if (isPlayer(entity, gameState())) {
            playerSprite = sprite
            centerAt(entity)
        }
    }

    private fun environmentTextureName(template: EntityTemplate) =
        with (template.spawnCondition!!) {
            val biomeName = biome!!.name.toLowerCase()
            val timeName: Any? = time?.name?.toLowerCase() ?: ""
            GAME_ENVIRONMENT_TEXTURE_TEMPLATE.format(biomeName, timeName)
        }

    private fun addSprite(sprite: Renderable, key:Long) {
        sprites[key] = sprite
        spritesOrderedByZ.add(sprite)
        sortSprites()
    }


    private fun removeEntities(entities: Iterable<Entity>) {
        for (entity in entities) {
            val key = entity.id
            val sprite = sprites[key] ?: continue;
            removeSprite(sprite, key)
        }
    }

    fun removeSprite(sprite: Renderable?, key: Long) {
        if (sprite == null) {
            return
        }

        spritesOrderedByZ.remove(sprite)
        sprites.remove(key)
    }


    private fun updateEntities(entities: Iterable<Displayable>) {
        for (entity in entities) {
            val sprite = sprites[entity.id] ?: continue;
            val gamePosition = entity.position
            val newScreenPosition = delegate.toScreenPoint(gamePosition)

            val screenPosition = sprite.position

            if (newScreenPosition != screenPosition) {
                sprite.move(newScreenPosition)
                sprite.color = Light.forPosition(gamePosition)
            }
        }

        //todo
//        val player = delegate.player
//        playerSprite.color = Light.forPosition(player.position)

//        sortSprites()
    }

    fun render() {
        updateEntities(delegate.entities)

        spritesOrderedByZ.forEach {
            it.render(delegate.spriteBatch)
        }
    }

    private fun sortSprites() {
        spritesOrderedByZ.sortBy { it.z }
//        spritesOrderedByZ.sort { s1, s2 -> s2.position.y.compareTo(s1.position.y) }
    }
}