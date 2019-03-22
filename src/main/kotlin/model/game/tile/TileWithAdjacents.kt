package model.game.tile

import model.game.chunk.Chunk


data class TileWithAdjacents(
        val tile: Tile,
        val tileUp: Tile?,
        val tileDown: Tile?,
        val tileLeft: Tile?,
        val tileRight: Tile?,

        val tileUpLeft: Tile?,
        val tileUpRight: Tile?,
        val tileDownLeft: Tile?,
        val tileDownRight: Tile?
) {

    val adjacents = listOf(tileDown, tileUp, tileLeft, tileRight, tileDownLeft, tileDownRight, tileUpLeft, tileUpRight)

    constructor(x: Int, y: Int, chunk: Chunk) : this(
            chunk.get(x, y)!!,
            chunk.get(x, y + 1),
            chunk.get(x, y - 1),
            chunk.get(x - 1, y),
            chunk.get(x + 1, y),
            chunk.get(x - 1, y + 1),
            chunk.get(x + 1, y + 1),
            chunk.get(x - 1, y - 1),
            chunk.get(x + 1, y - 1)
    )
}