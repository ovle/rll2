package view.imp.game.chunk

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.chunk.Chunk
import view.LAND_TEXTURE_TILE_SIZE_PX
import view.imp.game.chunk.tile.TileFactory
import view.imp.game.texture.GAME_TILES_TEXTURE
import view.imp.game.texture.TextureRegistry


enum class LayerType(val index: Int) {
    Background(0),
    Game(1),
    Special(2);

    companion object {
        fun comparator() = java.util.Comparator {
            type1: LayerType, type2: LayerType ->
            type1.index.compareTo(type2.index)
        }
    }
}


class ChunkView(val chunk: Chunk) {

    private val tileFactory = kodein.instance<TileFactory>()
    private val textureRegistry = kodein.instance<TextureRegistry>()

    val cellsByLayerType = mutableMapOf (
            LayerType.Background to cells2d(),
            LayerType.Game to cells2d(),
            LayerType.Special to cells2d()
    ).toSortedMap(LayerType.comparator())


    fun reload() {
        val chunkSize = chunk.size()

        val regions = TextureRegion.split(textureRegistry[GAME_TILES_TEXTURE], LAND_TEXTURE_TILE_SIZE_PX, LAND_TEXTURE_TILE_SIZE_PX)
        for (x in 0..chunkSize.width - 1) {
            for (y in 0..chunkSize.height - 1) {
                for ((layerType, cells) in cellsByLayerType) {
                    val tile = tileFactory.tile(x, y, chunk, layerType, regions) ?: continue
                    updateCell(tile, cells, x, y)
                }
            }
        }
    }

    private fun updateCell(tile: TiledMapTile, cells: Array<Array<TiledMapTileLayer.Cell>>, x: Int, y: Int) {
        val cell = TiledMapTileLayer.Cell()
        cell.tile = tile
        cells[x][y] = cell
    }

    fun draw(layer: TiledMapTileLayer, layerType: LayerType) {
        forEachCell(layerType) {
            x, y, cellX, cellY, cell ->
            layer.setCell(cellX, cellY, cell)
        }
    }

    fun erase(layer: TiledMapTileLayer, layerType: LayerType) {
        forEachCell(layerType) {
            x, y, cellX, cellY, cell ->
            layer.setCell(cellX, cellY, null)
        }
    }

    private fun forEachCell(layerType: LayerType, action: (x: Int, y: Int, cellX: Int, cellY: Int, cell: TiledMapTileLayer.Cell?) -> Unit) {
        val chunkSize = chunk.size()
        val index = chunk.index
        for ((cellsLayerType, cells) in cellsByLayerType) {
            if (cellsLayerType != layerType) continue

            for (x in 0..cells.size - 1) {
                for (y in 0..cells[x].size - 1) {
                    val cellX = x + chunkSize.width * index.x
                    val cellY = y + chunkSize.height * index.y

                    action(x, y, cellX, cellY, cells[x][y]);
                }

            }
        }
    }

    private fun cells2d() = Array(
            chunk.size().height, {
        Array(
                chunk.size().width, {
            TiledMapTileLayer.Cell()
        })
    })
}