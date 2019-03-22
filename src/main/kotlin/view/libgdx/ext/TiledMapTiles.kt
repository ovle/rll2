package view.libgdx.ext

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import view.imp.game.chunk.tile.TexturePart


interface Colored {
    var color: Color?
}

class BaseStaticTiledMapTile(
        textureRegion: TextureRegion?,
        override var color: Color? = null
) : StaticTiledMapTile(textureRegion), Colored {

    companion object {
        fun from(
                texturePart: TexturePart,
                textureTiles: Array<out Array<TextureRegion>>
        ): Array<out StaticTiledMapTile?> {
            val staticTiles = arrayOfNulls<BaseStaticTiledMapTile>(texturePart.points.size)
            texturePart.points.forEachIndexed { i, point ->
                //x-y reverted here
                val textureTile = textureTiles[point.y][point.x]
                staticTiles[i] = BaseStaticTiledMapTile(textureTile, texturePart.color)
            }
            return staticTiles
        }
    }
}

class BaseAnimatedTiledMapTile(
        interval: Float,
        frameTiles: com.badlogic.gdx.utils.Array<StaticTiledMapTile?>,
        override var color: Color? = null
) : AnimatedTiledMapTile(interval, frameTiles), Colored