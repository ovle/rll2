//package model.game.generation.impl
//
//import model.game.generation.MapGenerator
//import util.randomLessThan
//import java.awt.Dimension
//
//
//class SideOutdoorMapGenerator : MapGenerator {
//
//    private val SOLID = 0.5f
//    private val FLOOR_HEIGHT = 0.0f
//
//    override fun generate(size: Dimension): MapGenerator.MapGenerationResult {
//        val result = Array(size.width, { Array(size.height, {FLOOR_HEIGHT}) })
//        result.forEach { column ->
//            for (y in (0..2)) {
//                column[y]= SOLID
//            }
////            for (y in (3..size.height - 1)) {
////                if (column[y-1] == SOLID && randomLessThan(0.5f)) {
////                    column[y]= SOLID
////                }
////            }
//        }
//
//        return MapGenerator.MapGenerationResult(result)
//    }
//}