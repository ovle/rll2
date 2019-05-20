package launcher

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import model.game.GameState
import model.game.chunk.*
import model.game.generation.GridGenerator
import model.game.generation.impl.DungeonMapGenerator
import model.game.template.EntityTemplate
import model.game.template.ItemTemplate
import util.Registry
import util.registry
import view.config.SpritesheetConfig
import view.imp.game.chunk.tile.*
import view.imp.game.sprite.BaseSpriteFactory
import view.imp.game.sprite.SpriteFactory
import view.imp.game.texture.BaseTextureRegistry
import view.imp.game.texture.TextureRegistry


val kodein = Kodein {
    val entityTemplatesConfigPath = "resources/config/objects.json"
    val itemsConfigPath = "resources/config/items.json"
    val spriteSheetsConfigPath = "resources/images/spritesheet.json"

    bind<Registry<String, EntityTemplate>>() with
            singleton { registry<EntityTemplate>(entityTemplatesConfigPath) }
    bind<Registry<String, ItemTemplate>>() with
            singleton { registry<ItemTemplate>(itemsConfigPath) }
    bind<Registry<String, SpritesheetConfig>>() with
            singleton { registry<SpritesheetConfig>(spriteSheetsConfigPath) }

    bind<SpriteFactory>() with singleton { BaseSpriteFactory() }
    bind<TileFactory>() with singleton { BaseTileFactory() }

    bind<GridGenerator>() with singleton { DungeonMapGenerator() }

    bind<TextureRegistry>() with singleton { BaseTextureRegistry() }
    bind<TexturePartFactory>() with singleton { TopDownTexturePartFactory() }

    bind<ChunkFactory>() with singleton { BaseChunkFactory() }
    bind<ChunkRegistry>() with singleton { BaseChunkRegistry() }

    bind<EntitiesFactory>() with singleton { BaseEntitiesFactory() }

    bind() from singleton { GameState() } //todo
}

fun entityTemplateRegistry() = kodein.instance<Registry<String, EntityTemplate>>()
fun chunkRegistry() = kodein.instance<ChunkRegistry>()
fun entityFactory() = kodein.instance<EntitiesFactory>()
fun gameState() = kodein.instance<GameState>()
