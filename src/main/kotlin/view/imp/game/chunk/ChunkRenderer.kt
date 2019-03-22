package view.imp.game.chunk

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import model.game.chunk.Chunk
import model.game.onChunksLoaded
import model.game.onChunksUnloaded
import java.awt.Point
import java.util.concurrent.ConcurrentHashMap


interface ChunkRendererDelegate {
    val layersByType:Map<LayerType, TiledMapTileLayer>
    val chunks: Map<Point, Chunk>
}

class ChunkRenderer(val delegate: ChunkRendererDelegate) {

    private var chunkViews = ConcurrentHashMap<Point, ChunkView>()

    init {

        onChunksLoaded {
            it.chunks.forEach {
                val view = ChunkView(it)
                view.reload()
                chunkViews[it.index] = view
            }
        }

        onChunksUnloaded {
            it.chunks.forEach {
                val view = chunkViews[it.index]
                for ((type, layer) in delegate.layersByType) {
                    view?.erase(layer, type)
                }

                chunkViews.remove(it.index)
            }
        }
    }

    fun render() {
        val chunks = delegate.chunks
        chunks.values.forEach {
            var view = chunkViews[it.index]
            //todo draw on change
            for ((type, layer) in delegate.layersByType) {
                view?.draw(layer, type)
            }
        }
    }
}