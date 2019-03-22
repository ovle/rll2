package view.config

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import util.Identifiable
import java.awt.Dimension
import java.awt.Point


@JsonIgnoreProperties(ignoreUnknown = true)
class SpritesheetConfig(
        @JsonProperty(value = "id")
        override val id: String,
        @JsonProperty(value = "regionSize")
        val regionSize: Dimension?,
        @JsonProperty(value = "sprites")
        val sprites: Array<SpriteConfig>?,
        @JsonProperty(value = "parts")
        val parts: Array<SpritePartConfig>?
        //todo
//        @JsonProperty(value = "partsPrototype")
//        val partsPrototype: String?
) : Identifiable<String> {
//        init {
//            if ((parts == null) == (partsPrototype == null)) {
//                throw IllegalStateException("only parts or only partsPrototype should be set")
//            }
//        }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class SpriteConfig(
        @JsonProperty(value = "id")
        val id: String,

        @JsonProperty(value = "index")
        val index: Int?,
        @JsonProperty(value = "origin")
        val origin: Point? = null,
        @JsonProperty(value = "size")
        val size: Dimension? = null,
        @JsonProperty(value = "offset")
        val offset: Point? = null
) {
        init {
            if ((origin == null) == (index == null)) {
                throw IllegalStateException("only origin or only index should be set")
            }
        }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SpritePartConfig(
        @JsonProperty(value = "id")
        val id: String,
        @JsonProperty(value = "indexX")
        val indexX: Int,
        @JsonProperty(value = "Y")
        val Y: Int,
        @JsonProperty(value = "height")
        val height: Int?,
        @JsonProperty(value = "offset")
        val offset: Point? = null,
        @JsonProperty(value = "animations")
        val animations: Array<SpritePartAnimationConfig>?
)

class SpritePartAnimationConfig(
        @JsonProperty(value = "id")
        val id: String,
        @JsonProperty(value = "startIndexX")
        val startIndexX: Int,
        @JsonProperty(value = "startIndexY")
        val startIndexY: Int,
        @JsonProperty(value = "steps")
        val steps: Int,
        @JsonProperty(value = "frameDuration")
        val frameDuration: Float,
        @JsonProperty(value = "repeat")
        val repeat: Boolean = false,
        @JsonProperty(value = "terminal")
        val terminal: Boolean = false
)