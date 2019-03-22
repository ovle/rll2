package view.imp.game.chunk.tile

import model.game.tile.Tile
import model.game.tile.TileWithAdjacents
import util.point
import view.imp.game.chunk.LayerType
import java.awt.Point


interface TexturePartFactory {
    fun textureParts(tiles: TileWithAdjacents, layerType: LayerType): Array<TexturePart>?
}


class SideTexturePartFactory : TexturePartFactory {

    enum class TileTextureType(val pointsProducer: () -> Array<Point>) {
        Solid({ point(1, 0) }),
        Void({ point(0, 1) }),
        Water({ point(1, 1)})
    }

    override fun textureParts(tiles: TileWithAdjacents, layerType: LayerType): Array<TexturePart>? {
//        fun biomeIndex(tile: Tile) = (Biome.biome(tile)).ordinal
        fun water(tile: Tile?) = (tile != null) && tile.type == Tile.Type.Water
        fun bgClose(tile: Tile?) = (tile != null) && tile.y < 6
        fun solid(tile: Tile?) = (tile != null) && tile.type == Tile.Type.Solid
        fun randomLessThan(chance: Float) = Math.random() < chance

        val tile = tiles.tile
        var type = when (layerType) {
            LayerType.Game -> {
                when {
                    solid(tile) -> TileTextureType.Solid
                    water(tile) -> TileTextureType.Water
                    else -> null
                }
            }
            LayerType.Background -> TileTextureType.Void

            else -> null
        }
        type ?: return null

        return arrayOf(TexturePart(points = type.pointsProducer()))
    }
}