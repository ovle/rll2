//package model.game.generation.impl
//
//import lombok.extern.log4j.Log4j
//import model.game.generation.MapGenerator
//import model.game.generation.util.MapNormalizer
//import java.awt.Dimension
//import java.awt.Point
//import java.awt.Rectangle
//import java.util.*
//
//@Log4j
//class FractalMapGenerator(
//
//        /**
//         * noise value that depends on current area size
//         */
//        val flexibleNoiseValue: Float = 0.0f,
//
//        /**
//         * main generation parameter
//         */
//        val startIteration: Int = 1,
//        val stopIteration: kotlin.Int = -1,
//
//        val randomizeLastIteration: Boolean = true,
//
//        val normalize: Boolean = false,
//        val isToroidal: Boolean = false
//) : MapGenerator {
//
//    companion object {
//        val MIN_AREA_SIZE: Int = 3
//        val TILE_NOT_INITIALIZED_ID: Float = -1.0f
//    }
//
//    private val random: Random = Random()
//
//    /**
//     * grid values of global map
//     * array 3x3
//     *todo
//     * */
//    private var gridValues: Array<Array<Float>>? = null
//
//
//    private var areaLength: Int = 0
//    /**
//     * borders of neighbors
//     */
//    private var neighborValues: Array<Array<Float>>? = null
//
//
//    //todo refactor all
////---------------------------------------------------------------------------------------------
////	Entry point
////---------------------------------------------------------------------------------------------
//
//    override fun generate(size: Dimension, neighbors: MapGenerator.MapNeighbors?, gridValues: MapGenerator.GridValues?): MapGenerator.MapGenerationResult {
//        areaLength = size.width
//        this.gridValues = gridValues?.values
//        val result = Array(size.width, { Array(size.height, {TILE_NOT_INITIALIZED_ID})})
//
//        if (neighbors!= null) {
//            fillInitialValues(neighbors, size)
//        }
//        initMap(result)
//
//        processArea(result, 0, 0, size.width, size.height, startIteration)
//
//        if (normalize) {
//            val normalizer = MapNormalizer();
//            normalizer.process(result);
//        }
//
//        return MapGenerator.MapGenerationResult(result)
//    }
//
//    private fun fillInitialValues(neighbors: MapGenerator.MapNeighbors, size: Dimension) {
//        val left = neighbors.left
//        val right = neighbors.right
//        val top = neighbors.top
//        val bottom = neighbors.bottom
//
//        neighborValues = Array(size.width, { Array(size.height, { TILE_NOT_INITIALIZED_ID }) })
//
//        val length = neighborValues!!.size - 1
//
//        for (row in neighborValues!!.indices) {
//            if (left != null) {
//                neighborValues!![0][row] = left[length][row]
//            }
//            if (right != null) {
//                neighborValues!![length][row] = right[0][row]
//            }
//        }
//
//        for (column in neighborValues!![0].indices) {
//            if (top != null) {
//                neighborValues!![column][length] = top[column][0]
//            }
//            if (bottom != null) {
//                neighborValues!![column][0] = bottom[column][length]
//            }
//        }
//    }
//
////---------------------------------------------------------------------------------------------
////	Map initialization
////---------------------------------------------------------------------------------------------
//
//    private fun initMap(map: Array<Array<Float>>) {
//        val width = map.size - 1
//        val height = map[0].size - 1
//
//        if (gridValues != null) {
//            applyGridValues(height, map, width)
//        }
//
//        if (neighborValues != null) {
//            applyNeighborValues(height, map, width)
//        }
//
//        initCorners(height, map, width)
//    }
//
//    private fun applyGridValues(height: Int, map: Array<Array<Float>>, width: Int) {
//        val halfWidth = width / 2
//        val halfHeight = height / 2
//
//        map[0][0] = gridValues!![0][0]
//        map[0][height] = gridValues!![0][2]
//        map[width][0] = gridValues!![2][0]
//        map[width][height] = gridValues!![2][2]
//
//        map[0][halfHeight] = gridValues!![0][1]
//        map[halfWidth][0] = gridValues!![1][0]
//        map[width][halfHeight] = gridValues!![2][1]
//        map[halfWidth][height] = gridValues!![1][2]
//
//        map[halfWidth][halfHeight] = gridValues!![1][1]
//    }
//
//    private fun applyNeighborValues(height: Int, map: Array<Array<Float>>, width: Int) {
//        for (i in 0..width) {
//            for (j in 0..height) {
//                if (neighborValues!![i][j] == TILE_NOT_INITIALIZED_ID) {
//                    continue
//                }
//                map[i][j] = neighborValues!![i][j]
//            }
//        }
//    }
//
//    private fun initCorners(height: Int, map: Array<Array<Float>>, width: Int) {
//        if (map[0][0] == TILE_NOT_INITIALIZED_ID) {
//            map[0][0] = random.nextFloat()
//        }
//        if (map[width][0] == TILE_NOT_INITIALIZED_ID) {
//            map[width][0] = random.nextFloat()
//        }
//        if (map[0][height] == TILE_NOT_INITIALIZED_ID) {
//            map[0][height] = random.nextFloat()
//        }
//        if (map[width][height] == TILE_NOT_INITIALIZED_ID) {
//            map[width][height] = random.nextFloat()
//        }
//    }
//
////---------------------------------------------------------------------------------------------
////	Map area initialization
////---------------------------------------------------------------------------------------------
//
//    private fun initMapArea(mapArea: Array<Array<Float>>, actualArea: Rectangle) {
//        val width = actualArea.width - 1
//        val height = actualArea.height - 1
//        val maxX = actualArea.x + width
//        val maxY = actualArea.y + height
//
//        var value = 0.0f
//        if (!this.isTileInitialized(mapArea, actualArea.x, actualArea.y)) {
//            value = random.nextFloat()
//            mapArea[actualArea.x][actualArea.y] = value
//            if (isToroidal) {
//                if (actualArea.x == 0) {
//                    mapArea[mapArea.size - 1][actualArea.y] = value
//                }
//                if (actualArea.y == 0) {
//                    mapArea[actualArea.x][mapArea[0].size - 1] = value
//                }
//            }
//        }
//        if (!this.isTileInitialized(mapArea, maxX, actualArea.y)) {
//            value = random.nextFloat()
//            mapArea[maxX][actualArea.y] = value
//            if (isToroidal) {
//                if (actualArea.y == 0) {
//                    mapArea[maxX][mapArea[0].size - 1] = value
//                }
//            }
//        }
//        if (!this.isTileInitialized(mapArea, actualArea.x, maxY)) {
//            value = random.nextFloat()
//            mapArea[actualArea.x][maxY] = value
//            if (isToroidal) {
//                if (actualArea.x == 0) {
//                    mapArea[mapArea.size - 1][maxY] = value
//                }
//            }
//        }
//        if (!this.isTileInitialized(mapArea, maxX, maxY)) {
//            value = random.nextFloat()
//            mapArea[maxX][maxY] = value
//        }
//    }
//
////---------------------------------------------------------------------------------------------
////	Helpers
////---------------------------------------------------------------------------------------------
//
//    private fun isTileInitialized(areaTiles: Array<Array<Float>>, x: Int, y: Int): Boolean {
//        return (areaTiles[x][y] != TILE_NOT_INITIALIZED_ID)
//    }
//
//    private fun addNeighbour(mapArea: Array<Array<Float>>, x: Int, y: Int, neighbours: ArrayList<Float>) {
//        if (!validatePoint(mapArea, x, y)) {
//            return
//        }
//        if (mapArea[x][y] == TILE_NOT_INITIALIZED_ID) {
//            return
//        }
//
//        neighbours.add(mapArea[x][y])
//    }
//
//    private fun validatePoint(cells: Array<Array <Float>>, x: Int, y: Int): Boolean {
//        if ((x < 0) || (x >= cells.size)) {
//            return false
//        }
//        if ((y < 0) || (y >= cells[x].size)) {
//            return false
//        }
//
//        return true
//    }
//
//
//    private fun setTileValue(mapArea: Array<Array<Float>>, x: Int, y: Int, isBorderX: Boolean, isBorderY: Boolean, cellWidth: Int, cellHeight: Int) {
//        //corner points stay unaffected
//        if (isBorderX && isBorderY) {
//            return
//        }
//        //if already have value - return
//        if (this.isTileInitialized(mapArea, x, y)) {
//            return
//        }
//
//        val isCenterPoint = (!isBorderX && !isBorderY)
//        val halfWidth = cellWidth / 2
//        val halfHeight = cellHeight / 2
//        val leftX = x - halfWidth
//        val rightX = x + halfWidth
//        val bottomY = y + halfHeight
//        val topY = y - halfHeight
//
//        val currentTileNeighbours = ArrayList<Float>()
//
//        if (isCenterPoint) {
//            addNeighbour(mapArea, leftX, topY, currentTileNeighbours)
//            addNeighbour(mapArea, leftX, bottomY, currentTileNeighbours)
//            addNeighbour(mapArea, rightX, topY, currentTileNeighbours)
//            addNeighbour(mapArea, rightX, bottomY, currentTileNeighbours)
//        } else {
//            addNeighbour(mapArea, x, topY, currentTileNeighbours)
//            addNeighbour(mapArea, x, bottomY, currentTileNeighbours)
//            addNeighbour(mapArea, leftX, y, currentTileNeighbours)
//            addNeighbour(mapArea, rightX, y, currentTileNeighbours)
//        }
//
//        val neighboursCount = currentTileNeighbours.size
//        if (neighboursCount > 0) {
//            val randomLimit = random.nextInt(cellWidth) - cellWidth / 2
//            val randomOffset = randomLimit.toFloat() / areaLength.toFloat()
//
//            val shouldSelectRandomNeighbour = randomizeLastIteration && ((cellWidth == MIN_AREA_SIZE) || (cellHeight == MIN_AREA_SIZE))
//
//            var result = 0.0f
//            if (shouldSelectRandomNeighbour) {
//                val neighbourIndex = random.nextInt(neighboursCount)
//                result = (currentTileNeighbours[neighbourIndex])
//            } else {
//                //main part
//                var totalValue = 0.0f
//                for (i in 0..neighboursCount - 1) {
//                    totalValue += currentTileNeighbours[i]
//                }
//                val midValue = totalValue / neighboursCount
//                result = midValue
//            }
//            result += (randomOffset * flexibleNoiseValue)
//            mapArea[x][y] = result
//
//            if (isToroidal) {
//                if (x == 0) {
//                    mapArea[mapArea.size - 1][y] = result
//                }
//                if (y == 0) {
//                    mapArea[x][mapArea[0].size - 1] = result
//                }
//            }
//        }
//    }
//
////---------------------------------------------------------------------------------------------
////	Main algorithm method
////---------------------------------------------------------------------------------------------
//
//    //todo recursive?
//    private fun processArea(mapArea: Array<Array<Float>>, startX: Int, startY: Int, initCellWidth: Int, initCellHeight: Int, startIteration: Int) {
//        var cellWidth = initCellWidth
//        var cellHeight = initCellHeight
//        var shouldStop = false
//        var iterationsCount = 0
//
//        while (!shouldStop) {
//            val minWidthReached = (cellWidth < MIN_AREA_SIZE)
//            val minHeightReached = (cellHeight < MIN_AREA_SIZE)
//            shouldStop = minWidthReached && minHeightReached
//            if (shouldStop) {
//                break
//            }
//
//            if (iterationsCount == stopIteration) {
//                break
//            }
//
//            val cellsInGridRow = mapArea.size / (cellWidth - 1)
//            val cellsInGridColumn = mapArea[0].size / (cellHeight - 1)
//            val useRandomFilling = (iterationsCount < startIteration)
//            //TODO: save first cycle params to not calculate twice
//            //calc square center point value
//            for (i in 0..cellsInGridRow - 1) {
//                for (j in 0..cellsInGridColumn - 1) {
//                    val x = startX + (cellWidth - 1) * i
//                    val y = startY + (cellHeight - 1) * j
//
//                    val middleX = x + cellWidth / 2
//                    val middleY = y + cellHeight / 2
//
//                    //System.out.println("process middle point " + middleX + "," + middleY);
//                    if (!useRandomFilling) {
//                        //main option
//                        setTileValue(mapArea, middleX, middleY, false, false, cellWidth, cellHeight)
//                    } else {
//                        initMapArea(mapArea, Rectangle(x, y, cellWidth, cellHeight))
//                    }
//                }
//            }
//
//            //calc borders middle points values
//            for (i in 0..cellsInGridRow - 1) {
//                for (j in 0..cellsInGridColumn - 1) {
//                    val x = startX + (cellWidth - 1) * i
//                    val y = startY + (cellHeight - 1) * j
//
//                    val middleX = x + cellWidth / 2
//                    val middleY = y + cellHeight / 2
//
//                    val maxX = x + cellWidth - 1
//                    val maxY = y + cellHeight - 1
//
//                    //System.out.println("process square = " + new Rectangle(x, y, cellWidth, cellHeight));
//                    if (!useRandomFilling) {
//                        setTileValue(mapArea, middleX, y, false, true, cellWidth, cellHeight)
//                        setTileValue(mapArea, middleX, maxY, false, true, cellWidth, cellHeight)
//                        setTileValue(mapArea, x, middleY, true, false, cellWidth, cellHeight)
//                        setTileValue(mapArea, maxX, middleY, true, false, cellWidth, cellHeight)
//                    } else {
//                        val cellInitWidth = cellWidth / 2 + 1
//                        val cellInitHeight = cellHeight / 2 + 1
//                        initMapArea(mapArea, Rectangle(x, y, cellInitWidth, cellInitHeight))
//                        initMapArea(mapArea, Rectangle(middleX, y, cellInitWidth, cellInitHeight))
//                        initMapArea(mapArea, Rectangle(x, middleY, cellInitWidth, cellInitHeight))
//                        initMapArea(mapArea, Rectangle(middleX, middleY, cellInitWidth, cellInitHeight))
//                    }
//                }
//            }
//
//            iterationsCount++
//            cellWidth = (cellWidth / 2 + 1)
//            cellHeight = (cellHeight / 2 + 1)
//        }
//    }
//}
