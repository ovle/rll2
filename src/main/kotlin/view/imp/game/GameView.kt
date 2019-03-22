package view.imp.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.github.salomonbrys.kodein.instance
import launcher.kodein
import model.game.GameState
import model.game.chunk.Chunk
import model.game.entity.Entity
import model.game.entity.traits.CollisionSubject
import model.game.entity.traits.Displayable
import utils.geom.Point
import view.*
import view.imp.game.chunk.ChunkRenderer
import view.imp.game.chunk.ChunkRendererDelegate
import view.imp.game.chunk.LayerType
import view.imp.game.texture.TextureRegistry
import view.libgdx.ext.DynamicTiledMapLayer


class GameView(val game: GameState) : ScreenView(), SpriteRendererDelegate, ChunkRendererDelegate {

    private val spriteRenderer = SpriteRenderer(this)
    private val chunkRenderer = ChunkRenderer(this)
    private val shapeRenderer = ShapeRenderer()

    //--------------------------------------------------

    lateinit var logArea: TextArea
    //--------------------------------------------------

    lateinit var chunksView: TiledMapView
    override val chunks: Map<java.awt.Point, Chunk>
        get() = game.chunks
    //--------------------------------------------------

    override val layersByType = mutableMapOf (
            LayerType.Background to layer(),
            LayerType.Game to layer(),
            LayerType.Special to layer()
    ).toSortedMap(LayerType.comparator())

    private fun layer(): TiledMapTileLayer = DynamicTiledMapLayer(0, 0, 12, 12)

    //--------------------------------------------------

    override val player: Entity
        get() = game.player
    override val entities: Collection<Displayable>
        get() = game.entities


    override fun toScreenPoint(position: Point): Point {
        val playerPosition = game.player.position
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        val screenX = (position.x - playerPosition.x) * PIXELS_PER_TILE + screenWidth * PLAYER_SCREEN_WIDTH_OFFSET_RATIO;
        val screenY = (position.y - playerPosition.y) * PIXELS_PER_TILE + screenHeight * PLAYER_SCREEN_HEIGHT_OFFSET_RATIO;

        return Point(screenX, screenY)// - scrollOffset
    }

    fun toGamePoint(screenPosition: java.awt.Point): Point {
        val playerPosition = game.player.position
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        val x = (screenPosition.x - screenWidth * PLAYER_SCREEN_WIDTH_OFFSET_RATIO)/ PIXELS_PER_TILE + playerPosition.x
        val y = (- screenPosition.y + screenHeight * (1 - PLAYER_SCREEN_HEIGHT_OFFSET_RATIO))/ PIXELS_PER_TILE + playerPosition.y

        return Point(x, y)
    }

    override fun scrollCamera(x: Float, y: Float) {
        camera.translate(x, y)
    }

    //--------------------------------------------------

    override fun initIntr() {
        super.initIntr()

        chunksView = TiledMapView(GLOBAL_SCALE)
        chunksView.init(layersByType.values)

        initGUI()
    }

    private fun initGUI() {
        //todo
        val panel = Image(skin, "default-pane")
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        panel.setSize(screenWidth, 100f)
        addActor(panel)

        logArea = TextArea(null , skin)
        logArea.setBounds(200f, 10f, 200f, 80f)
        addActor(logArea)
    }

    override fun disposeIntr() {
        kodein.instance<TextureRegistry>().dispose()
        chunksView.dispose()
    }

    override fun renderIntr(delta: Float) {
        camera.update()

        renderMap(delta)
        //todo
//        renderShapes()
    }

    private fun renderShapes() {
        shapeRenderer.projectionMatrix = camera.combined;
        shapeRenderer.color = Color.LIGHT_GRAY
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)

        for (entity in entities) {
            if (entity is CollisionSubject) {
                shapeRenderer.renderBox(entity.box)
            }
        }
        shapeRenderer.renderBox(player.box)

        shapeRenderer.end()
    }

    fun ShapeRenderer.renderBox(box: Rectangle) =
        box.apply {
            val scale = PIXELS_PER_TILE
            rect(
                    x * scale,
                    y * scale,
                    width * scale,
                    height * scale
            )
        }


    private fun renderMap(delta: Float) {
        chunkRenderer.render()
        chunksView.render(camera, delta)
    }

    override fun renderSpritesIntr(delta: Float, batch: SpriteBatch?) {
        spriteRenderer.render()
    }

    //--------------------------------------------------

    //todo
//    override fun onEntityChangeActionTargets(entity: Entity, addedTargets: Collection<ActionTarget>, removedTargets: Collection<ActionTarget>) {
//        logArea.text = if ((addedTargets + removedTargets).isEmpty()) ""
//            else """
//            selected: ${addedTargets.map { it.name }.joinToString()}
//            deselected: ${removedTargets.map { it.name }.joinToString()}
//            """
//
//        spriteRenderer.onEntityChangeActionTargets(entity, addedTargets, removedTargets)
//    }
}