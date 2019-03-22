package view.imp.game.chunk.tile

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.chunk.Chunk
import model.game.tile.TileWithAdjacents
import view.imp.game.chunk.LayerType


interface TileFactory {
    fun tile(x: Int, y: Int, chunk: Chunk, layerType: LayerType, regions: Array<Array<TextureRegion>>): TiledMapTile?
}


class BaseTileFactory(): TileFactory {

    private val texturePartFactory = kodein.instance<TexturePartFactory>()

    override fun tile(x: Int, y: Int, chunk: Chunk, layerType: LayerType, regions: Array<Array<TextureRegion>>): TiledMapTile? {
        val textureParams = texturePart(x, y, chunk, layerType)
        textureParams ?: return null

        val tile = chunk.get(x, y)
        val tileParams = textureParams.map { it.toTile(regions, tile) }
        if (tileParams.isEmpty()) return null

        return tileParams.first()
    }

    private fun texturePart(x: Int, y: Int, chunk: Chunk, layerType: LayerType): Array<TexturePart>? {
        val tiles = TileWithAdjacents(x, y, chunk)

        return texturePartFactory.textureParts(tiles, layerType)
    }
}