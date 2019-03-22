package view.imp.game.texture

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture


val GAME_TILES_TEXTURE = "resources/images/tiles.png"
val WORLD_TILES_TEXTURE = GAME_TILES_TEXTURE
val GAME_CHARS_TEXTURE = "resources/images/chars.png"
val GAME_ITEMS_TEXTURE = "resources/images/items.png"
val GAME_MISC_TEXTURE = "resources/images/misc.png"
val GAME_ENVIRONMENT_TEXTURE_TEMPLATE = "resources/images/environment/%s_%s.png"
val GAME_CHARS_PORTRAIT_TEXTURE = "resources/images/portraits.png"


/**
 * Loads and holds textures
 */
interface TextureRegistry {

    operator fun get(key: String): Texture

    fun dispose()
}

class BaseTextureRegistry: TextureRegistry {

    private val textures: MutableMap<String, Texture> = mutableMapOf()

    override fun get(key: String): Texture {
        var result = textures[key]
        if (result == null) {
            result = Texture(Gdx.files.internal(key))
            textures.put(key, result)
        }
        return result
    }

    override fun dispose() {
        textures.forEach { it.value.dispose() }
        textures.clear()
    }
}