package model.game.chunk

import java.awt.Rectangle

/**
 * Any semantic area of chunk (e.g. room)
 * only rectangular form is supported
 */
open class ChunkArea(val rectangle: Rectangle)