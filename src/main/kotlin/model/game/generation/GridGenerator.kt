package model.game.generation

import com.github.czyzby.noise4j.map.Grid
import java.awt.Dimension


interface GridGenerator {

    data class Result(val grid: Grid, val size: Int)

    fun generate(size: Int): Result
}