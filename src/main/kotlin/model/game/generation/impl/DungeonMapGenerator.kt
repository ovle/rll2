package model.game.generation.impl

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import model.game.generation.MapGenerator
import java.awt.Dimension


class DungeonMapGenerator: MapGenerator {

    override fun generate(size: Dimension): MapGenerator.Result {
        val grid = Grid(64)

        DungeonGenerator().apply {
            roomGenerationAttempts = 500
            maxRoomSize = 30
            tolerance = 10 // Max difference between width and height.
            minRoomSize = 9
        }.generate(grid)

        return MapGenerator.Result(grid)
    }
}