package view.imp.game.chunk.tile

import model.game.tile.Tile
import model.game.tile.TileWithAdjacents
import util.animation
import util.point
import util.randomPoint
import view.imp.game.chunk.LayerType
import java.awt.Point

/**
 * Returns texture part by given game tile
 */
class TopDownTexturePartFactory : TexturePartFactory {

    enum class TileTextureType(val pointsProducer: () -> Array<Point>) {
        Void({ point(0, 0) }),
        Wall({ randomPoint(0..2, 1) }),
        WallColumn({ point(4, 1) }),
        WallItem({ point(3, 1) }),
        Floor({ randomPoint(0..4, 2) }),
        Door({ point(5, 1) }),
        FloorHole({ point(5, 2) }),
        Water({ animation(0..4, 4)});
    }

    override fun textureParts(tiles: TileWithAdjacents, layerType: LayerType): Array<TexturePart>? {
//        fun biomeIndex(tile: Tile) = (Biome.biome(tile)).ordinal
        fun water(tile: Tile?) = (tile != null) && tile.type == Tile.Type.Water
        fun wall(tile: Tile?) = (tile != null) && tile.type == Tile.Type.Solid
        fun randomLessThan(chance: Float) = Math.random() < chance

        val tile = tiles.tile
        var type = when (layerType) {
            LayerType.Game -> {
                when {
                    wall(tile) -> when {
                        !wall(tiles.tileDown) -> when {
                            randomLessThan(0.1f) -> TileTextureType.WallItem
                            (!wall(tiles.tileLeft) || !wall(tiles.tileRight)) -> TileTextureType.WallColumn
                            else -> TileTextureType.Wall
                        }
                        (tiles.adjacents.any { !wall(it) })-> TileTextureType.Wall
                        else -> null
                    }
                    water(tile) -> TileTextureType.Water
                    else -> TileTextureType.Floor
                }
            }
            else -> null
        }
        type ?: return null

        return arrayOf(TexturePart(points = type.pointsProducer()))
    }
}