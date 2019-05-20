package model.game.chunk

import model.game.CHUNK_SIZE_IN_TILES
import model.game.tile.Tile
import utils.geom.Point
import java.awt.Dimension
import java.io.Serializable

/**
 * A rectangular chunk of world's terrain
 */
class Chunk(
        val index: java.awt.Point,
        val tiles: Tiles,
        val areas: Collection<ChunkArea> = mutableListOf<ChunkArea>()
) : Serializable {

    fun get(position: Point) = get(
            (position.x - (index.x * CHUNK_SIZE_IN_TILES)).toInt(),
            (position.y - (index.y * CHUNK_SIZE_IN_TILES)).toInt()
    )

    fun get(x: Int, y: Int): Tile? {
        if (!tiles.isValid(x, y)) return null

        return tiles[x, y]
    }

    fun size() = Dimension(tiles.size, tiles.size)

    fun forEachTile(consumer: (Tile, Int, Int) -> (Unit)) = tiles.forEachTile(consumer)
}