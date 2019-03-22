package model

import java.awt.Dimension


val WORLD_SIZE = Dimension(65, 65)

class World(val size: Dimension) {}

class Player {
    private val DEFAULT_CHAR_ID = "rogue"
    var charId = DEFAULT_CHAR_ID
}