package thefireplace.gltest.world

import org.joml.Matrix4f
import org.joml.Vector3f
import thefireplace.gltest.io.Window
import thefireplace.gltest.render2d.Camera
import thefireplace.gltest.render.Shader
import thefireplace.gltest.render2d.TileRenderer

class World(val width: Int, val height: Int, val scale: Float) {
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

    //This equasion will need to change if you
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