package model.game.chunk

import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.CHUNK_SIZE_IN_TILES
import java.awt.Point
import java.util.concurrent.ConcurrentHashMap


interface ChunkRegistry {
    operator fun get(indexes: Point): Chunk
    fun clear()
}

class BaseChunkRegistry: ChunkRegistry {

    private val chunkFactory = kodein.instance<ChunkFactory>()

    private val chunks = ConcurrentHashMap<java.awt.Point, Chunk>()


    override fun get(indexes: Point): Chunk {
        var result = getOrNull(indexes)
        if (result == null) {
            result = create(indexes)
            chunks[indexes] = result
        }
        return result
    }

    private fun create(indexes: Point): Chunk {
//        val neighbors = ChunkFactory.ChunkNeighbors(
//                left = getOrNull(Point(indexes.x - 1, indexes.y)),
//                right = getOrNull(Point(indexes.x + 1, indexes.y)),
//                top = getOrNull(Point(indexes.x, indexes.y + 1)),
//                bottom = getOrNull(Point(indexes.x, indexes.y - 1))
//        )

//        if (WorldData.current == null) {
//            val errorMessage = "no current world"
//            throw IllegalStateException(errorMessage)
//        }

//        val chunkLeftTopWorldPoint = indexes * 2
//        val gridValues = ChunkFactory.ChunkGridValues(
//                WorldData.current!!.tiles,
//                chunkLeftTopWorldPoint
//        )
//
//        val result = ChunkFactory.create(
//                indexes,
//                Constants.CHUNK_SIZE_IN_TILES,
//                neighbors,
//                gridValues
//        )

        return chunkFactory.create(indexes, CHUNK_SIZE_IN_TILES)
    }

    private fun getOrNull(indexes: Point) = chunks[indexes]

    override fun clear() = chunks.clear()
}