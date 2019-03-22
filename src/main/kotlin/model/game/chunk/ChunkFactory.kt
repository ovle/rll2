package model.game.chunk

import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.generation.MapGenerator
import model.game.tile.Tile
import java.awt.Dimension


interface ChunkFactory {
    fun create(index: java.awt.Point, size: Dimension): Chunk
}


class BaseChunkFactory: ChunkFactory {

    private val mapGenerator = kodein.instance<MapGenerator>()

    override fun create(index: java.awt.Point, size: Dimension): Chunk {
        val generationResult = mapGenerator.generate(size)
        return Chunk(index, tiles(generationResult))
    }

    //todo
    private fun tiles(generationResult: MapGenerator.Result): Array<Array<out Tile>>
            = generationResult.grid.run {
                val result = arrayOf<Array<out Tile>>()
                array.for
                return@run result
            }

    private fun type(value: Float): Tile.Type {
        return when {
            //todo constant
            value >= 0.5 -> Tile.Type.Solid
            else -> Tile.Type.Void
        }
    }
}