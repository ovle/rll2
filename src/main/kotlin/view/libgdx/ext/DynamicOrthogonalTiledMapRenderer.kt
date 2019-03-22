package view.libgdx.ext

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer


class DynamicOrthogonalTiledMapRenderer(map: TiledMap?, unitScale: Float) : OrthogonalTiledMapRenderer(map, unitScale) {

    override fun renderTileLayer(layer: TiledMapTileLayer) {
        val batchColor = this.batch.color
        var defaultColor = batchColor.toFloat(layer.opacity)
        val layerWidth = layer.width
        val layerHeight = layer.height
        val layerTileWidth = layer.tileWidth * this.unitScale
        val layerTileHeight = layer.tileHeight * this.unitScale

        //todo
        val mapLayer = layer as DynamicTiledMapLayer
        val col1 = Math.max(mapLayer.size.minX, (this.viewBounds.x / layerTileWidth).toInt())
        val col2 = Math.min(layerWidth, ((this.viewBounds.x + this.viewBounds.width + layerTileWidth) / layerTileWidth).toInt())
        val row1 = Math.max(mapLayer.size.minY, (this.viewBounds.y / layerTileHeight).toInt())
        val row2 = Math.min(layerHeight, ((this.viewBounds.y + this.viewBounds.height + layerTileHeight) / layerTileHeight).toInt())
        var y = row2.toFloat() * layerTileHeight
        val xStart = col1.toFloat() * layerTileWidth

        val vertices = this.vertices

        var color = defaultColor;
        for (row in row2 downTo row1) {
            var x = xStart

            for (col in col1..col2 - 1) {
                val cell = layer.getCell(col, row)
                if (cell == null) {
                    x += layerTileWidth
                } else {
                    val tile = cell.tile
                    if (tile != null) {
                        if (tile is Colored) {
                            val tileColor = tile.color
                            color = if (tileColor != null) tileColor.toFloat(layer.opacity) else defaultColor
                        }
                        val flipX = cell.flipHorizontally
                        val flipY = cell.flipVertically
                        val rotations = cell.rotation
                        val region = tile.textureRegion
                        val x1 = x + tile.offsetX * this.unitScale
                        val y1 = y + tile.offsetY * this.unitScale
                        val x2 = x1 + region.regionWidth.toFloat() * this.unitScale
                        val y2 = y1 + region.regionHeight.toFloat() * this.unitScale
                        val u1 = region.u
                        val v1 = region.v2
                        val u2 = region.u2
                        val v2 = region.v
                        vertices[0] = x1
                        vertices[1] = y1
                        vertices[2] = color
                        vertices[3] = u1
                        vertices[4] = v1
                        vertices[5] = x1
                        vertices[6] = y2
                        vertices[7] = color
                        vertices[8] = u1
                        vertices[9] = v2
                        vertices[10] = x2
                        vertices[11] = y2
                        vertices[12] = color
                        vertices[13] = u2
                        vertices[14] = v2
                        vertices[15] = x2
                        vertices[16] = y1
                        vertices[17] = color
                        vertices[18] = u2
                        vertices[19] = v1
                        var tempV: Float
                        if (flipX) {
                            tempV = vertices[3]
                            vertices[3] = vertices[13]
                            vertices[13] = tempV
                            tempV = vertices[8]
                            vertices[8] = vertices[18]
                            vertices[18] = tempV
                        }

                        if (flipY) {
                            tempV = vertices[4]
                            vertices[4] = vertices[14]
                            vertices[14] = tempV
                            tempV = vertices[9]
                            vertices[9] = vertices[19]
                            vertices[19] = tempV
                        }

                        if (rotations != 0) {
                            var tempU: Float
                            when (rotations) {
                                1 -> {
                                    tempV = vertices[4]
                                    vertices[4] = vertices[9]
                                    vertices[9] = vertices[14]
                                    vertices[14] = vertices[19]
                                    vertices[19] = tempV
                                    tempU = vertices[3]
                                    vertices[3] = vertices[8]
                                    vertices[8] = vertices[13]
                                    vertices[13] = vertices[18]
                                    vertices[18] = tempU
                                }
                                2 -> {
                                    tempV = vertices[3]
                                    vertices[3] = vertices[13]
                                    vertices[13] = tempV
                                    tempV = vertices[8]
                                    vertices[8] = vertices[18]
                                    vertices[18] = tempV
                                    tempU = vertices[4]
                                    vertices[4] = vertices[14]
                                    vertices[14] = tempU
                                    tempU = vertices[9]
                                    vertices[9] = vertices[19]
                                    vertices[19] = tempU
                                }
                                3 -> {
                                    tempV = vertices[4]
                                    vertices[4] = vertices[19]
                                    vertices[19] = vertices[14]
                                    vertices[14] = vertices[9]
                                    vertices[9] = tempV
                                    tempU = vertices[3]
                                    vertices[3] = vertices[18]
                                    vertices[18] = vertices[13]
                                    vertices[13] = vertices[8]
                                    vertices[8] = tempU
                                }
                            }
                        }

                        this.batch.draw(region.texture, vertices, 0, 20)
                    }

                    x += layerTileWidth
                }
            }

            y -= layerTileHeight
        }

    }

    private fun Color.toFloat(opacity: Float) = Color.toFloatBits(this.r, this.g, this.b, this.a * opacity)
}