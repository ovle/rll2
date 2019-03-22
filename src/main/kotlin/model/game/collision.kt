package model.game

import com.badlogic.gdx.math.Rectangle
import model.game.chunk.Chunk
import model.game.entity.Entity
import model.game.tile.Tile
import utils.geom.Point


fun collisionTiles(box: Rectangle, gameState: GameState): Collection<Tile> {
    val minX = box.x
    val minY = box.y
    val maxX = minX + box.width
    val maxY = minY + box.height

    var minXInt = minX.toInt()
    if (minX < 0) minXInt -= 1
    var minYInt = minY.toInt()
    if (minY < 0) minYInt -= 1

    var maxXInt = maxX.toInt()
    if (maxX < 0) maxXInt -= 1
    var maxYInt = maxY.toInt()
    if (maxY < 0) maxYInt -= 1

    val xRange = (minXInt..maxXInt)
    val yRange = (minYInt..maxYInt)

    val result = mutableListOf<Tile>()
    for (x in xRange) {
        for (y in yRange) {
            val tile = tile(x, y, gameState) ?: continue
            if (!tile.passable) {
                result.add(tile)
            }
        }
    }

    return result
}

private fun collisionEntities(box: Rectangle, gameState: GameState, entity: Entity) =
        gameState.entities.filter { it != entity && !it.template.passable && it.box.overlaps(box) }

private fun collideWithTiles(box: Rectangle, gameState: GameState) = collisionTiles(box, gameState).isNotEmpty()

private fun collideWithEntities(box: Rectangle, gameState: GameState, entity: Entity) = collisionEntities(box, gameState, entity).isNotEmpty()

fun collideWithAny(entity: Entity, point: Point, gameState: GameState): Boolean {
    val testBox = Rectangle(point.x.toFloat(), point.y.toFloat(), entity.box.width, entity.box.height)
    return collideWithTiles(testBox, gameState) || collideWithEntities(testBox, gameState, entity)
}

//todo
fun emptyTilePosition(initialChunks: List<Chunk>): Point {
    val initialChunk = initialChunks.find { it.index.x == 0 }
    val emptyTiles = mutableListOf<Point>()
    initialChunk!!.forEachTile { tile, x, y ->  if (tile.passable) emptyTiles.add(Point(x.toDouble(), y.toDouble())) }
    return emptyTiles.first()
}