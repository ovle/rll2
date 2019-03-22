package model.game

import launcher.chunkRegistry
import model.game.chunk.Chunk
import model.game.entity.Entity
import model.game.tile.Tile
import util.toIndex
import utils.geom.Point


fun tile(x: Int, y: Int, gameState: GameState): Tile? {
    val point = Point(x.toDouble(), y.toDouble())
    return gameState.chunks[point.toIndex()] ?.get(point)
}

fun checkChunksToUnload(gameState: GameState) {
    var chunksToUnload = gameState.chunks.size - MAX_LOADED_CHUNKS_COUNT
    while(chunksToUnload-- > 0) {
        unloadChunk(getChunkToUnload(gameState.player, gameState), gameState)
    }
}

fun checkChunksToLoad(dx: Double, dy: Double, entity: Entity, gameState: GameState) {
    val pointsToCheck = getPointsToCheck(dx, dy, entity)
    pointsToCheck.forEach {
        val index = it.toIndex()
        val chunk = gameState.chunks[index]
        val needLoadChunk = chunk == null && !gameState.chunksLoading.contains(index)
        if (needLoadChunk) {
            loadChunk(index, gameState)
        }
    }
}

fun loadChunk(index: java.awt.Point, gameState: GameState) {
    gameState.chunksLoading.add(index)

    runAndAccept(
            { chunkRegistry()[index] },
            {
                gameState.chunks.put(index, it)
                gameState.chunksLoading.remove(index)

                loadEntities(it, gameState)

                chunksLoaded(listOf(it))
            })
}

fun getChunkToUnload(target: Entity, gameState: GameState): Chunk? {
    val targetIndex = target.position.toIndex()
    var unloadIndex: java.awt.Point? = null
    var maxDistance = Double.MIN_VALUE

    gameState.chunks.keys.forEach {
        val distance = it.distance(targetIndex)
        if (distance > maxDistance) {
            maxDistance = distance
            unloadIndex = it
        }
    }

    return gameState.chunks[unloadIndex]
}


fun unloadChunk(chunk: Chunk?, gameState: GameState) {
    if (chunk == null) {
        return
    }

    unloadEntities(chunk, gameState)
    gameState.chunks.remove(chunk.index)

    chunksUnloaded(listOf(chunk))
}

fun getPointsToCheck(dx: Double, dy: Double, entity: Entity): Array<Point> {
    val position = entity.position
    val absX = Math.abs(dx)
    val absY = Math.abs(dy)
    val d = if (dx > 0) absX else absY

    val pointsToCheck = arrayOf(
            //straight hor/vert
            position.translate(
                    dx = absX * CHUNK_LOAD_DISTANCE_CELLS,
                    dy = absY * CHUNK_LOAD_DISTANCE_CELLS
            ),
            //diag 1
            position.translate(
                    dx = d * CHUNK_LOAD_DISTANCE_CELLS / 2,
                    dy = d * CHUNK_LOAD_DISTANCE_CELLS / 2
            ),
            //diag 2
            position.translate(
                    dx = d * CHUNK_LOAD_DISTANCE_CELLS / 2,
                    dy = -d * CHUNK_LOAD_DISTANCE_CELLS / 2
            )
    )

    return pointsToCheck
}

fun initialChunks(): List<Chunk> {
    val startChunkIndex = java.awt.Point(0, 0)
    val initialChunks = with(chunkRegistry()) {
        listOf(
                this[java.awt.Point(startChunkIndex).apply { x -= 1 }],
                this[startChunkIndex],
                this[java.awt.Point(startChunkIndex).apply { x += 1 }])
    }
    return initialChunks
}

operator fun Chunk.contains(entity: Entity): Boolean {
    val chunkIndex = this.index
    val entityIndex = entity.position.toIndex()
    return chunkIndex == entityIndex;
}
