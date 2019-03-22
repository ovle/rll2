package view.imp

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.github.salomonbrys.kodein.instance
import launcher.kodein
import utils.Direction
import view.ScreenView
import view.TiledMapView
import view.imp.game.texture.TextureRegistry
import view.imp.game.texture.WORLD_TILES_TEXTURE
import view.libgdx.ext.DynamicTiledMapLayer


class WorldView(val delegate: WorldViewDelegate) : ScreenView() {

    companion object {
        val WORLD_TILE_SIZE: Int = 1
        val VIEW_SCALE: Float = 4.0f
    }

    private val textureRegistry = kodein.instance<TextureRegistry>()


    lateinit private var mapTexture: Texture
    lateinit private var mapView: TiledMapView
    private val layer = DynamicTiledMapLayer(400, 400, WORLD_TILE_SIZE, WORLD_TILE_SIZE)

    lateinit private var cells: Array<Array<TiledMapTileLayer.Cell>>


    override fun initIntr() {
        super.initIntr()

        mapTexture = textureRegistry.get(WORLD_TILES_TEXTURE)
        mapView = TiledMapView(VIEW_SCALE)
        mapView.init(listOf(layer))

        //todo
//        val nameInput = TextField("world", skin)
//        nameInput.setPosition(600.0f, Gdx.graphics.height - nameInput.height - 10)
//        addActor(nameInput)

//        val rightPane = WidgetGroup(s)
//        addActor(btn)

        reload()
    }

    fun reload() {
        val splitTiles = TextureRegion.split(mapTexture, 1, 1)
//        val tiles = delegate.worldData.tiles
//        cells = Array(tiles.size,
//                {Array(tiles[0].size,
//                        { TiledMapTileLayer.Cell() }) })
//
//        for (x in tiles.indices) {
//            for (y in tiles[x].indices) {
//                val texturePart = WorldTiles.worldTexturePart(tiles[x][y])
//                val cell = TiledMapTileLayer.Cell()
////                with (texturePart) {
////                    //x-y reverted here
////                    cell.tile = BaseStaticTiledMapTile(splitTiles[point.y][point.x], color)
////                }
//
//                cells[x][y] = cell
//            }
//        }
//
//        updateTiledMap()
    }


    override fun renderIntr(delta: Float) {
        camera.update()
        mapView.render(camera, delta)
    }


    fun updateTiledMap() {
        for (x in cells.indices) {
            for (y in cells[x].indices) {
                layer.setCell(x, y, cells[x][y])
            }
        }
    }

    override fun disposeIntr() {
        textureRegistry.dispose()
        mapView.dispose()
    }

    fun scrollTiles(direction: Direction, delta: Float) {
        mapView.scroll(direction, delta)
    }
}