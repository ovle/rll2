package model.game

import com.github.salomonbrys.kodein.instance
import launcher.entityFactory
import launcher.entityTemplateRegistry
import launcher.kodein
import model.game.chunk.Chunk
import model.game.entity.Entity
import model.game.entity.item.Item
import model.game.template.ItemTemplate
import util.Registry
import utils.geom.Point


fun entitiesAt(gamePoint: Point, gameState: GameState) = gameState.entities.filter { it.box.contains(gamePoint.x.toFloat(), gamePoint.y.toFloat()) }

fun entity(id: String): Entity {
    val template = entityTemplateRegistry()[id]!!
    val result = entityFactory().entity(template)
    return result
}

fun loadEntities(chunk: Chunk, gameState: GameState) {
    val loadedEntities = entityFactory().entities(chunk)
    loadEntities(loadedEntities, gameState)
}

fun loadEntities(entities: Collection<Entity>, gameState: GameState) {
    entitiesLoaded(entities)
    gameState.entities.addAll(entities)
}

fun unloadEntities(chunk: Chunk, gameState: GameState) {
    val entitiesToUnload = gameState.entities.filter { it in chunk }
    gameState.entities.removeAll(entitiesToUnload)
    entitiesUnloaded(entitiesToUnload)
}

//todo
fun loadHouse(initialChunks: List<Chunk>, gameState: GameState) {
    val house = entity("house1")
    house.position = emptyTilePosition(initialChunks)

    val door = entity("door1")
    door.position = house.position.translate(dx = 1.5)

    loadEntities(listOf(house, door), gameState)
}

//    private fun applyGravity(entity: Entity, amount: Float) {
//        if (needApplyGravity(entity, amount)) {
//            moveEntity(entity, Direction.Down, amount)
//        }
//    }
//
//    private fun needApplyGravity(entity: Entity, amount: Float): Boolean {
//        return canMoveAt(entity, entity.position.plus(Point(0.0, amount.toDouble())))
//    }

fun player(spawnableChunks: List<Chunk>): Entity {
    val itemTemplateRegistry = kodein.instance<Registry<String, ItemTemplate>>()
    return entity("human").apply {
        position = emptyTilePosition(spawnableChunks)

        inventory.items.add(Item(itemTemplateRegistry["plate"]!!))
        inventory.items.removeAll { it.template.name == "basicCloth" }
    }
}

fun isPlayer(entity: Entity, gameState: GameState) = entity == gameState.player

////todo
//fun toggleSelection(activeEntity: Entity, gamePoint: Point) {
//    val newTargets = entitiesAt(gamePoint)
//    val currentTargets = activeEntity.currentTargets
//    val targetsToAdd = newTargets - currentTargets
//    val targetsToDelete = currentTargets.intersect(newTargets)
//    activeEntity.addTargets(targetsToAdd)
//    activeEntity.removeTargets(targetsToDelete)
//
////        entityDelegate.onEntityChangeActionTargets(activeEntity, targetsToAdd, targetsToDelete)
//}