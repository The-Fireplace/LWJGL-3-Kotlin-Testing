package thefireplace.gltest.world

import org.joml.Matrix4f
import org.joml.Vector3f
import thefireplace.gltest.io.Window
import thefireplace.gltest.render2d.Camera
import thefireplace.gltest.render.Shader
import thefireplace.gltest.render2d.TileRenderer
import javax.imageio.ImageIO

class World(private val width: Int, private val height: Int, val scale: Float) {
    //Constructor using a level file
    constructor(level:String, scale:Float): this(getLevelWidth(level), getLevelHeight(level), scale){
        val tileSheet = ImageIO.read(this::class.java.getResourceAsStream("/levels/$level.png"))
        val colorSheet = tileSheet.getRGB(0, 0, width, height, null, 0, width)

        for(y in 0 until height)
            for(x in 0 until width){
                val color = colorSheet[x + y * width]
                val r = ((Math.pow(256.0, 3.0) + color) / 65536).toInt()
                val g = ((Math.pow(256.0, 3.0) + color) / 256 % 256).toInt()
                val b = ((Math.pow(256.0, 3.0) + color) % 256).toInt()
                val rgb = r shl 16 or (g shl 8) or b
                val tile = TileRegistry.getTile(rgb)
                if(tile != null)
                    setTile(tile, x, y)
                else {
                    val rgbStr = Integer.toHexString(rgb)
                    error("Tile not found for color $rgbStr.")
                }
            }
    }

    companion object {
        fun getLevelWidth(level:String):Int{
            return ImageIO.read(this::class.java.getResourceAsStream("/levels/$level.png")).width
        }
        fun getLevelHeight(level:String):Int{
            return ImageIO.read(this::class.java.getResourceAsStream("/levels/$level.png")).height
        }
    }

    val tiles = IntArray(width * height)

    val world: Matrix4f = Matrix4f().setTranslation(Vector3f(0f)).scale(scale)

    fun render(render: TileRenderer, shader: Shader, camera: Camera) {
        for (i in 0 until height)
            for (j in 0 until width) {
                val tile = getTile(j, i)
                if (tile != null)
                    render.renderTile(tile, j - width / 2, -i + height / 2, shader, world, camera)
            }
    }

    fun setTile(tile: Tile, x: Int, y: Int) {
        tiles[x + y * width] = tile.getId()
    }

    //This equation will need to change if you
    // a.) translate the world itself rather than the camera
    // b.) change the model vertices
    // c.) change the ortho of the camera projection
    fun correctCamera(camera: Camera, window: Window) {
        val pos = camera.getPosition()
        val w = -width * scale * 2
        val h = -height * scale * 2

        if (-2 * w < window.getWidth()) {//Map is smaller than window
            if (pos.x > (w + window.getWidth()) / 2 + scale)
                pos.x = (w + window.getWidth()) / 2 + scale
            else if (pos.x < -(w + window.getWidth()) / 2 + scale)
                pos.x = -(w + window.getWidth()) / 2 + scale
        } else {//Map is larger than/equal to window
            if (pos.x > window.getWidth() / 2 + scale)
                pos.x = window.getWidth() / 2 + scale
            else if (pos.x < -window.getWidth() / 2 + scale)
                pos.x = -window.getWidth() / 2 + scale
        }

        if (-2 * h < window.getHeight()) {//Map is smaller than window
            if (pos.y > (h + window.getHeight()) / 2 - scale)
                pos.y = (h + window.getHeight()) / 2 - scale
            else if (pos.y < -(h + window.getHeight()) / 2 - scale)
                pos.y = -(h + window.getHeight()) / 2 - scale
        } else {//Map is larger than window/equal to
            if (pos.y > window.getHeight() / 2 - scale)
                pos.y = window.getHeight() / 2 - scale
            else if (pos.y < -window.getHeight() / 2 - scale)
                pos.y = -window.getHeight() / 2 - scale
        }
    }

    fun getTile(x: Int, y: Int): Tile? {
        return try {
            TileRegistry.getTile(tiles[x + y * width])
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }
}