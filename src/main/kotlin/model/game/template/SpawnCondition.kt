package model.game.template

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import model.game.tile.TileWithAdjacents
import model.game.time.DayTimeValue

@JsonIgnoreProperties(ignoreUnknown=true)
class SpawnCondition (
        @JsonProperty(value = "chance")
        val chance: Float = 1.0f,
        @JsonProperty(value = "biome")
        val biome: Biome? = null,
        @JsonProperty(value = "time")
        val time: DayTimeValue? = null
) {
    fun check(tiles: TileWithAdjacents) = chance > Math.random() && checkIntr(tiles)

    fun checkIntr(tiles: TileWithAdjacents): Boolean {
        return tiles.tile.passable && !(tiles.tileDown?.passable ?: false)
    }
}