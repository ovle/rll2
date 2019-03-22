package view

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import utils.Direction
import view.libgdx.ext.DynamicOrthogonalTiledMapRenderer

/**
 * Thin wrapper over Tiled Map
 * Updating is external (using exposed layer)
 */
class TiledMapView(val scale: Float) {

    lateinit protected var mapRenderer: TiledMapRenderer
    lateinit protected var tiledMap: TiledMap

//    protected var scrollOffset: java.awt.Point = java.awt.Point(0, 0)


    fun init(layers:Collection<TiledMapTileLayer>) {

        tiledMap = TiledMap()
        layers.forEach { tiledMap.layers.add(it) }

        mapRenderer = DynamicOrthogonalTiledMapRenderer(tiledMap, scale)
    }

    fun render(camera: OrthographicCamera, delta: Float) {
        mapRenderer.setView(camera)
        mapRenderer.render()
    }

    fun dispose() {
        tiledMap.dispose()
    }

    fun scroll(to: Direction, delta: Float) {
        val scrollStep = 10
        val step = (delta * scrollStep).toDouble();
        val dx = -to.dx(step).toInt()
        val dy = -to.dy(step).toInt()
        //todo
//        scrollOffset.x += dx
//        scrollOffset.y += dy
    }
}