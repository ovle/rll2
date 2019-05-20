package model.game.chunk

import com.github.salomonbrys.kodein.*
import launcher.*
import model.game.generation.*
import model.game.tile.*


interface ChunkFactory {
    fun create(index: java.awt.Point, size: Int): Chunk
}

data class Tiles(private val tiles: Array<Tile>, internal val size: Int) {

    fun isValid(x: Int, y: Int) = index(x, y).run { this >= 0 && this < tiles.size }

    operator fun get(x: Int, y: Int): Tile? = tiles[index(x, y)]

    private fun index(x: Int, y: Int) = x * size + y

    fun forEachTile(consumer: (Tile, Int, Int) -> (Unit)) {
        for (i in tiles.indices) {
            val x = i / size
            val y = i % size
            this[x, y]?.let { consumer.invoke(it, x, y) }
        }
    }
}

class BaseChunkFactory: ChunkFactory {

    private val mapGenerator = kodein.instance<GridGenerator>()

    override fun create(index: java.awt.Point, size: Int): Chunk {
        val generationResult = mapGenerator.generate(size)
        return Chunk(index, tiles(generationResult))
    }

    //todo
    private fun tiles(result: GridGenerator.Result): Tiles {
        return Tiles(result.grid.array.map {
            Tile(0, 0, type(it))
        }.toTypedArray(), result.size)
    }

    private fun type(value: Float): Tile.Type {
        return when {
            //todo constant
            value >= 1.0 -> Tile.Type.Solid
            value >= 0.5 -> Tile.Type.Floor
            else -> Tile.Type.Void
        }
    }
}