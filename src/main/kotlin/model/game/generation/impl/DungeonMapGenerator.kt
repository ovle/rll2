package model.game.generation.impl

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import model.game.generation.GridGenerator


class DungeonMapGenerator: GridGenerator {

    override fun generate(size: Int): GridGenerator.Result {
        val grid = Grid(size)

        DungeonGenerator().apply {
            roomGenerationAttempts = 100
            maxRoomSize = 15
            tolerance = 10 // Max difference between width and height.
            minRoomSize = 3
        }.generate(grid)

        return GridGenerator.Result(grid, size)
    }
}