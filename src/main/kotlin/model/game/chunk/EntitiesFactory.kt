package model.game.chunk

import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.CHUNK_SIZE_IN_TILES
import model.game.entity.Entity
import model.game.entity.ai.AI
import model.game.entity.ai.BaseAI
import model.game.entity.item.Inventory
import model.game.entity.item.Item
import model.game.entity.traits.ActionHelper
import model.game.template.EntityTemplate
import model.game.template.ItemTemplate
import model.game.tile.TileWithAdjacents
import util.Registry
import utils.geom.Point
import java.util.*
import java.util.concurrent.atomic.AtomicLong


interface EntitiesFactory {
    fun entity(template: EntityTemplate): Entity
    fun entities(chunk: Chunk): Collection<Entity>
}

class BaseEntitiesFactory: EntitiesFactory {

    private val entityTemplateRegistry = kodein.instance<Registry<String, EntityTemplate>>()
    private val itemTemplateRegistry = kodein.instance<Registry<String, ItemTemplate>>()

    //todo unique per world
    private var currentId = AtomicLong(0)

    override fun entities(chunk: Chunk): List<Entity> {
        val result = mutableListOf<Entity>()
//        chunk.forEachTile {
//            tile, x, y ->
//                val tiles = TileWithAdjacents(x, y, chunk)
//                result.addAll(initTileEntities(chunk, tiles, x, y))
//        }
        return result
    }

    override fun entity(template: EntityTemplate): Entity {
        val actionHelper = ActionHelper()

        return Entity(currentId.incrementAndGet(), template, actionHelper, inventory(template), ai(template))
    }

    private fun ai(template: EntityTemplate): AI? {
        val isCreature = template.type == EntityTemplate.Type.Creature
        return if (isCreature) BaseAI() else null
    }

    private fun inventory(template: EntityTemplate): Inventory {
        val items = if (template.id != "human") mutableListOf()
            else mutableListOf(item("shortSword"), item("basicClothes"))
        return Inventory(items)
    }

    private fun item(id: String) = Item(itemTemplateRegistry[id]!!)

    private fun initTileEntities(chunk: Chunk, tiles: TileWithAdjacents, x: Int, y: Int): List<Entity> {
        val spawnableTemplates = entityTemplateRegistry.all().filter { it.canSpawnOn(tiles) }
        if (spawnableTemplates.isEmpty()) {
            return listOf()
        }

        val index = Random().nextInt(spawnableTemplates.size)
        val template = spawnableTemplates[index]

        val entity = entity(chunk, template, x, y)
        return listOf(entity)
    }

    private fun entity(chunk: Chunk, template: EntityTemplate, x: Int, y: Int): Entity {
        val entity = entity(template)
        entity.position = startPosition(chunk, x, y)

        return entity
    }

    private fun startPosition(chunk: Chunk, x: Int, y: Int): Point {
        val chunkPosition = chunk.index
        val chunkSize = CHUNK_SIZE_IN_TILES

        val entityX = chunkPosition.x * chunkSize.toDouble() + x
        val entityY = chunkPosition.y * chunkSize.toDouble() + y

        return Point(entityX, entityY)
    }
}