package model.game

import model.game.chunk.Chunk
import model.game.entity.Entity
import model.game.entity.action.Action
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass

sealed class Event {
    open class EntityEvent(val entity: Entity) : Event()
    class EntityStartMove(entity: Entity) : EntityEvent(entity)
    class EntityFinishMove(entity: Entity) : EntityEvent(entity)
    class EntityMoved(entity: Entity, val dx: Double, val dy: Double) : EntityEvent(entity)
    class EntityDidAction(entity: Entity, val action: Action) : EntityEvent(entity)
    class ChunksLoaded(val chunks: Collection<Chunk>) : Event()
    class ChunksUnloaded(val chunks: Collection<Chunk>) : Event()
    class EntitiesLoaded(val entities: Collection<Entity>) : Event()
    class EntitiesUnloaded(val entities: Collection<Entity>) : Event()
}

fun chunksLoaded(chunks: Collection<Chunk>) = Messenger.publish(Event.ChunksLoaded(chunks))
fun chunksUnloaded(chunks: Collection<Chunk>) = Messenger.publish(Event.ChunksUnloaded(chunks))
fun entitiesLoaded(entities: Collection<Entity>) = Messenger.publish(Event.EntitiesLoaded(entities))
fun entitiesUnloaded(entities: Collection<Entity>) = Messenger.publish(Event.EntitiesUnloaded(entities))
fun entityStartMove(entity: Entity) = Messenger.publish(Event.EntityStartMove(entity))
fun entityFinishMove(entity: Entity) = Messenger.publish(Event.EntityFinishMove(entity))
fun entityMoved(entity: Entity,  dx: Double, dy: Double) = Messenger.publish(Event.EntityMoved(entity, dx, dy))
fun entityDidAction(entity: Entity, action: Action) = Messenger.publish(Event.EntityDidAction(entity, action))

fun onChunksLoaded(callback: (Event.ChunksLoaded) -> Unit) = Messenger.subscribe<Event.ChunksLoaded>(callback)
fun onChunksUnloaded(callback: (Event.ChunksUnloaded) -> Unit) = Messenger.subscribe<Event.ChunksUnloaded>(callback)
fun onEntitiesLoaded(callback: (Event.EntitiesLoaded) -> Unit) = Messenger.subscribe<Event.EntitiesLoaded>(callback)
fun onEntitiesUnloaded(callback: (Event.EntitiesUnloaded) -> Unit) = Messenger.subscribe<Event.EntitiesUnloaded>(callback)
fun onEntityStartMove(callback: (Event.EntityStartMove) -> Unit) = Messenger.subscribe<Event.EntityStartMove>(callback)
fun onEntityFinishMove(callback: (Event.EntityFinishMove) -> Unit) = Messenger.subscribe<Event.EntityFinishMove>(callback)
fun onEntityMoved(callback: (Event.EntityMoved) -> Unit) = Messenger.subscribe<Event.EntityMoved>(callback)
fun onEntityDidAction(callback: (Event.EntityDidAction) -> Unit) = Messenger.subscribe<Event.EntityDidAction>(callback)


private object Messenger {

    val subscribers = mutableMapOf<KClass<Event>, MutableCollection<(Event) -> Unit>>()

    fun publish(event: Event) {
        subscribers[event.javaClass.kotlin]?.forEach { it.invoke(event) }
    }

    inline fun <reified T : Event> subscribe(noinline callback: (T) -> Unit) {
        val clazz = T::class as KClass<Event>
        val eventSubscribers = subscribers[clazz]
                ?: mutableListOf<(Event) -> Unit>().apply { subscribers[clazz] = this }
        eventSubscribers.add(callback as (Event) -> Unit)
    }
}

fun <T> runAndAccept(
        supplier: () -> T,
        consumer: (T) -> Unit
) {
    CompletableFuture
            .supplyAsync {
                //Thread.sleep(2000)
                run(supplier)
            }.thenAccept(consumer)
}

//    fun handle(event: Event?) {
//        Api.gameApi.sendEvent(
//                event,
//                success = {},
//                error = {
//                    rollback(event, it)
//                }
//        )
//    }
//
//    private fun rollback(event: Event?, status: EventStatus) {
//        //todo
//        when (event) {
//            is EntityMoved -> event.entity.position = Point((status as EntityEventStatus).entity.position)
//        }
//    }