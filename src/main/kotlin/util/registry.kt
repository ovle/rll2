package util

interface Registry<K, V> {

    val items: MutableMap<K, MutableList<V>>

    operator fun get(key: K): V? = items[key]?.random()

    fun all(): Collection<V> = items.values.flatMap { it }
}

open class BaseRegistry<K, V: Identifiable<K>>: Registry<K, V> {

    override val items = mutableMapOf<K, MutableList<V>>()

    fun init(initItems: Iterable<Identifiable<K>>) {
        initItems.forEach {
            val key = it.id
            val list = items[key] ?: mutableListOf<V>()
            list.add(it as V)
            items.put(key, list)
        }
    }
}

inline fun <reified V: Identifiable<String>> registry(path: String): Registry<String, V> = BaseRegistry<String, V>().apply {
    init((loadList<V>(path)))
}