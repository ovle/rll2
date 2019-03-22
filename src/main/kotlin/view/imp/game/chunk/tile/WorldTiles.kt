package view.imp.game.chunk.tile

import com.badlogic.gdx.graphics.Color
import model.game.tile.Tile
import java.awt.Point

/**
 * Created by ovle on 07.01.2016.
 */
object WorldTiles {

    fun worldTexturePart(tile: Tile): TexturePart {
        val variant = 9 //todo
        return TexturePart(arrayOf(Point(variant, 0)))
    }
}