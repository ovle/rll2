package model.game.template

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import util.Identifiable


@JsonIgnoreProperties(ignoreUnknown = true)
class ItemTemplate(
        @JsonProperty(value = "id")
        override val id: String,
        @JsonProperty(value = "name")
        val name: String,
        @JsonProperty(value = "type")
        val type: ItemType,
        @JsonProperty(value = "subtype")
        val subtype: ItemSubtype
        //todo
//        @JsonProperty(value = "actions")
//        val actions: Collection<String>
): Identifiable<String>

enum class ItemType {
    Weapon,
    Clothes
}

enum class ItemSubtype {
    Sword,
    LightClothes
}

