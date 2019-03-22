package model.game.template

import com.badlogic.gdx.math.Rectangle
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import model.game.tile.TileWithAdjacents
import util.Identifiable

@JsonIgnoreProperties(ignoreUnknown = true)
class EntityTemplate(
        @JsonProperty(value = "id")
        override val id: String,
        @JsonProperty(value = "name")
        val name: String,
        @JsonProperty(value = "type")
        val type: EntityTemplate.Type,
        @JsonProperty(value = "box")
        val box: Rectangle,
        @JsonProperty(value = "moving")
        val moving: MoveConfig?,

        @JsonProperty(value = "spawnCondition")
        val spawnCondition: SpawnCondition? = null,

        @JsonProperty(value = "isTransparent")
        val transparent: Boolean? = null,
        @JsonProperty(value = "passable")
        val passable: Boolean = true
        ): Identifiable<String> {

    enum class Type {
        Creature,
        Environment,
        Item
    }

    fun canSpawnOn(tiles: TileWithAdjacents) = (spawnCondition?.check(tiles) ?: false)
}

class MoveConfig(
    @JsonProperty(value = "type")
    val type: MoveType,
    @JsonProperty(value = "speed")
    val speed: Double
) {
    enum class MoveType {
        None,
        Ground
    }
}