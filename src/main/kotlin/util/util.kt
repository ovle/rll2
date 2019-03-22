package util

import com.fasterxml.jackson.databind.ObjectMapper
import model.game.CHUNK_SIZE_IN_TILES
import model.game.template.EntityTemplate
import utils.geom.Point
import view.config.SpritesheetConfig
import java.awt.Dimension
import java.io.File
import java.util.*


fun point(x: Int, y: Int) = arrayOf(java.awt.Point(x, y))
fun randomPoint(xRange: IntRange, y: Int) = arrayOf(java.awt.Point(xRange.random(), y))
fun animation(xRange: IntRange, y: Int) = xRange.map { java.awt.Point(it, y) }.toTypedArray()

operator fun java.awt.Point.times(amount: Int) = java.awt.Point(x * amount, y * amount)
operator fun Point.times(amount: Double) = Point(x * amount, y * amount)
operator fun Dimension.times(amount: Int) = Dimension(width * amount, height * amount)
infix operator fun java.awt.Point.plus(offset: java.awt.Point?) = java.awt.Point(this.x + (offset?.x ?: 0), this.y + (offset?.y ?: 0))

fun IntRange.random() = Math.round(Math.random() * (this.last - this.first)).toInt() + this.first
fun <T> List<T>.random(): T? = if (this.isEmpty()) null else this[(0..size - 1).random()]
fun randomLessThan(chance: Float) = Math.random() < chance


fun Point.toIndex(): java.awt.Point {
    val chunkSize = CHUNK_SIZE_IN_TILES
    val x = this.x
    val y = this.y
    var indexX = (x / chunkSize.width).toInt()
    var indexY = (y / chunkSize.height).toInt()

    if (x < 0 && ((x % chunkSize.width) != 0.0)) {
        indexX -= 1
    }
    if (y < 0 && ((y % chunkSize.height) != 0.0)) {
        indexY -= 1
    }

    return java.awt.Point(indexX, indexY)
}

fun File.asString(): String? {
    val scanner = Scanner(this)
    val string = StringBuffer()
    while (scanner.hasNext()) {
        string.append(scanner.nextLine())
    }
    return string.toString()
}

inline fun <reified T: Any> loadList(path: String): List<T> {
    val config = File(path)
    val mapper = ObjectMapper();
    val type = mapper.typeFactory.constructCollectionType(List::class.java, T::class.java)
    val result: List<T> = mapper.readValue(config, type);
    return result
}

fun <T> Array<T>.mapInPlace(transform: (T) -> T) {
    for (i in this.indices) {
        this[i] = transform(this[i])
    }
}

interface Identifiable<T> {
    val id: T
}