package view

import launcher.AppConfig
import java.awt.Dimension

val DEFAULT_BTN_SIZE: Dimension = Dimension(AppConfig.width / 8, AppConfig.height / 16)

val LAND_TEXTURE_TILE_SIZE_PX: Int = 24
val GLOBAL_SCALE = 1.0f
val PIXELS_PER_TILE = 24 * GLOBAL_SCALE
val PLAYER_SCREEN_WIDTH_OFFSET_RATIO = 1.0f/2
val PLAYER_SCREEN_HEIGHT_OFFSET_RATIO = 1.0f/2

val WALK_ANIMATION_NAME: String = "walk"
val STAB_HIT_ANIMATION_NAME: String = "stab"
val SLASH_HIT_ANIMATION_NAME: String = "slash"
val INVOKE_ANIMATION_NAME: String = "invoke"
val COMMON_ANIMATION_NAME: String = "common"
val BLOCK_ANIMATION_NAME: String = "block"
val SHOOT_ANIMATION_NAME: String = "shoot"
val DIE_ANIMATION_NAME: String = "die"