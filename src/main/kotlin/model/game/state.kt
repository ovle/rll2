package model.game

import model.game.chunk.Chunk
import model.game.entity.Entity
import model.game.time.GameTime
import java.awt.Point
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet


@Target(AnnotationTarget.FUNCTION)
annotation class StateMutator

class GameState() {
    val time = GameTime(0, 0, 0)

    lateinit var player: Entity

    val entities = CopyOnWriteArrayList<Entity>()

    val chunks = ConcurrentHashMap<Point, Chunk>()

    val chunksLoading = CopyOnWriteArraySet<Point>()
}

fun init(gameState: GameState) {
    val initialChunks = initialChunks()
    for (chunk in initialChunks ) {
        gameState.chunks.put(chunk.index, chunk)
    }
    chunksLoaded(initialChunks)

    gameState.player = player(initialChunks)
    entitiesLoaded(listOf(gameState.player))

    loadHouse(initialChunks, gameState)
    for (chunk in initialChunks ) {
        loadEntities(chunk, gameState)
    }
}

fun update(gameState: GameState, delta: Float) {
    gameState.time.update((delta * 1000).toLong()) // todo

    gameState.entities.forEach {
        it.update(delta, gameState)
//            applyGravity(it, 0.1f)
    }
}