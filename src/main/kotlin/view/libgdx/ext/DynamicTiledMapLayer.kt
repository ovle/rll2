package view.libgdx.ext

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer


class DynamicTiledMapLayer(width: Int, height: Int, tileWidth: Int, tileHeight: Int) : TiledMapTileLayer(width, height, tileWidth, tileHeight) {

    data class Bounds(var minX: Int, var maxX: Int, var minY: Int, var maxY: Int)

    val size = Bounds(0, 0, 0, 0)

    val cells = mutableMapOf<Long, Cell>()

    override fun getCell(x: Int, y: Int): Cell? {
        val key = keyOf(x, y);
        return cells[key]
    }

    override fun setCell(x: Int, y: Int, cell: Cell?) {
        updateSize(x, y)
        val key = keyOf(x, y);
        if (cell == null) {
            cells.remove(key)
        } else {
            cells[key] = cell
        }
    }

    private fun updateSize(x: Int, y: Int) {
        size.apply {
            when {
                x < minX -> minX = x
                x > maxX -> maxX = x
                y < minY -> minY = y
                y > maxY -> maxY = y
            }
        }
    }

    private fun keyOf(x: Int, y: Int) = ((x.toLong()) shl 32) or (y.toLong() and 0xffffffffL)

    override fun getHeight(): Int {
        return size.maxY - size.minY
    }

    override fun getWidth(): Int {
        return size.maxX - size.minX
    }
}