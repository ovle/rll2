package screen

import com.badlogic.gdx.InputAdapter

open class BaseInputProcessor : InputAdapter() {

    enum class EventType {
        KeyUp,
        KeyDown,
        MouseKeyDown,
        MouseKeyUp,
        IsKeyPressed
    }

    private data class HandlerKey(
        val eventType: EventType,
        val key:Int
    )


    private val keysDown: MutableSet<Int> = mutableSetOf()

    private val keyDownHandleGroups: MutableSet<Set<Int>> = mutableSetOf()
    private val keyUpHandleGroups: MutableSet<Set<Int>> = mutableSetOf()
    private val mouseKeyDownHandleGroups: MutableSet<Set<Int>> = mutableSetOf()
    private val mouseKeyUpHandleGroups: MutableSet<Set<Int>> = mutableSetOf()

    private val handlers: MutableMap<HandlerKey, (Float, Int)-> Unit> = mutableMapOf()

    private val mouseMoveHandlers: MutableCollection<(Int, Int)-> Unit> = mutableListOf()


    fun handler(
            eventType: EventType,
            keys:Set<Int>,
            callback:(Float, Int)-> Unit
    ): BaseInputProcessor {
        when (eventType) {
            EventType.KeyUp -> keyUpHandleGroups.add(keys)
            EventType.KeyDown -> keyDownHandleGroups.add(keys)
            EventType.MouseKeyUp -> mouseKeyUpHandleGroups.add(keys)
            EventType.MouseKeyDown -> mouseKeyDownHandleGroups.add(keys)
            else -> { }
        }

        keys.forEach {
            handlers[HandlerKey(eventType, it)] = callback
        }

        return this
    }


    fun tick(delta: Float) {
        keysDown.forEach { handlers[HandlerKey(EventType.IsKeyPressed, it)]?.invoke(delta, it) }
    }


    /**
     * key down event handler
     * fires if there's no keys pressed down yet in given key group
     */
    override fun keyDown(key: Int): Boolean {
        val needInvoke = isNeedInvokeForGroups(key, keyDownHandleGroups)
        if (needInvoke) {
            handlers[HandlerKey(EventType.KeyDown, key)]?.invoke(0.0f, key)
        }

        keysDown.add(key)

        return super.keyDown(key)
    }

    /**
     * key up event handler
     * fires if there's no more keys pressed down in given key group
     */
    override fun keyUp(key: Int): Boolean {
        keysDown.remove(key)

        val needInvoke = isNeedInvokeForGroups(key, keyUpHandleGroups)
        if (needInvoke) {
            handlers[HandlerKey(EventType.KeyUp, key)]?.invoke(0.0f, key)
        }

        return super.keyUp(key)
    }

    private fun isNeedInvokeForGroups(key: Int, keyGroups: MutableSet<Set<Int>>): Boolean {
        val keyGroup = keyGroups.lastOrNull { it.contains(key) }
        return (keyGroup != null) && keyGroup.intersect(keysDown).isEmpty()
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        mouseMoveHandlers.forEach { it.invoke(screenX, screenY) }
        return super.mouseMoved(screenX, screenY)
    }

    fun mouseMoveHandler(function: (Int, Int) -> Unit) {
        mouseMoveHandlers.add(function)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        handlers[HandlerKey(EventType.MouseKeyDown, button)]?.invoke(0.0f, button)
        return super.touchDown(screenX, screenY, pointer, button)
    }
}