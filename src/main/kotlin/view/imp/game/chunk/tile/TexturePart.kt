package view.imp.game.chunk.tile

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import model.game.tile.Tile
import view.libgdx.ext.BaseAnimatedTiledMapTile
import view.libgdx.ext.BaseStaticTiledMapTile
import java.awt.Point

data class TexturePart(
        val points:Array<Point>,
        val frameTime:Float = 0.25f,
        val color: Color? = null) {

    fun toTile(textureTiles: Array<Array<TextureRegion>>, tile: Tile?):BaseAnimatedTiledMapTile? {
        if (tile == null) return null

        val staticTiles: Array<out StaticTiledMapTile?> = BaseStaticTiledMapTile.from(this, textureTiles)
        return BaseAnimatedTiledMapTile(frameTime, com.badlogic.gdx.utils.Array(staticTiles), color)
    }
}