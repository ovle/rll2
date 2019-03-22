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
        val tiles: Array<Array<out Tile>>,
        val areas: Collection<ChunkArea> = mutableListOf<ChunkArea>()
) : Serializable {

    fun get(position: Point) = get(
            (position.x - (index.x * CHUNK_SIZE_IN_TILES.width)).toInt(),
            (position.y - (index.y * CHUNK_SIZE_IN_TILES.height)).toInt()
    )

    fun get(x: Int, y: Int): Tile? {
        if (x >= tiles.size || x < 0) return null
        if (y >= tiles[x].size || y < 0) return null

        return tiles[x][y]
    }

    fun size() = Dimension(tiles.size, tiles[0].size)

    fun forEachTile(consumer: (Tile, Int, Int) -> (Unit)) {
        for (x in tiles.indices) {
            for (y in tiles[x].indices) {
                val tile = this.get(x, y)
                consumer.invoke(tile!!, x, y)
            }
        }
    }
}