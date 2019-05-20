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

    enum class TileTextureType(val pointsProducer: Map<LayerType, () -> Array<Point>>) {
        Void(
                mapOf(LayerType.Main to { randomPoint(0..0, 0..0) })
        ),
        Floor(
                mapOf(LayerType.Floor to { randomPoint(1..2, 0..0) })
        ),
        Wall(
                mapOf(
                        LayerType.Main to { randomPoint(2..2, 2..2) },
                        LayerType.Top to { randomPoint(0..0, 3..3) }
                )
        )
//        WallColumn({ point(4, 1) }),
//        WallItem({ point(3, 1) }),
//        Door({ point(5, 1) }),
//        FloorHole({ point(5, 2) }),
//        Water({ animation(1..1, 1)});
    }

    override fun textureParts(tiles: TileWithAdjacents, layerType: LayerType): Array<TexturePart>? {
//        fun biomeIndex(tile: Tile) = (Biome.biome(tile)).ordinal
        fun void(tile: Tile?) = (tile == null) || tile.type == Tile.Type.Void
        fun wall(tile: Tile?) = (tile != null) && tile.type == Tile.Type.Solid
        fun floor(tile: Tile?) = (tile != null) && tile.type == Tile.Type.Floor
        fun randomLessThan(chance: Float) = Math.random() < chance

        val tile = tiles.tile
        val type = if (void(tile)) null
        else
            when (layerType) {
                LayerType.Main -> {
                    when {
                        wall(tile) -> TileTextureType.Wall
                        else -> null
                    }
                }
                LayerType.Floor -> {
                    when {
                        floor(tile) -> TileTextureType.Floor
                        else -> null
                    }
                }
                LayerType.Top -> {
                    when {
                        wall(tile) -> TileTextureType.Wall
                        else -> null
                    }
                }
            else -> null
        }
        type ?: return null

        return arrayOf(TexturePart(points = type.pointsProducer[layerType]?.invoke() ?: point(0, 0)))
    }
}