package model.game.generation

import com.github.czyzby.noise4j.map.Grid
import java.awt.Dimension


interface MapGenerator {

    data class Result(val grid: Grid, val props: Map<Any, Any?> = mapOf())

    fun generate(size: Dimension): Result
}