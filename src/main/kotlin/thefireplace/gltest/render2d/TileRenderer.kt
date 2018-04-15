package thefireplace.gltest.render2d

import org.joml.Matrix4f
import org.joml.Vector3f
import thefireplace.gltest.render.Shader
import thefireplace.gltest.world.Tile

class TileRenderer {
    private val tile_textures = HashMap<String, Texture>()
    private val model: Model

    init {
        //Vertices needed to make a quad which takes up half the screen.
        val vertices = floatArrayOf(
                -1f, 1f, 0f,//TOP LEFT 0
                1f, 1f, 0f,//TOP RIGHT 1
                1f, -1f, 0f,//BOTTOM RIGHT 2
                -1f, -1f, 0f//BOTTOM LEFT 3
        )

        //Texture coordinates
        val texture = floatArrayOf(
                0f, 0f,//TOP LEFT 0
                0f, 1f,//TOP RIGHT 1
                1f, 1f,//BOTTOM RIGHT 2
                1f, 0f//BOTTOM LEFT 3
        )

        //vertex and texture indices
        val indices = intArrayOf(
                0, 1, 2,//Top Right Triangle
                2, 3, 0//Bottom Left Triangle
        )

        //Create our VBO
        model = Model(vertices, texture, indices)
    }

    fun renderTile(tile: Tile, x: Int, y: Int, shader: Shader, world: Matrix4f, camera: Camera) {
        shader.useShader()
        val tex = tile.getTexture()
        if (tile_textures.containsKey(tex))
            tile_textures[tex]?.bind(0)

        val tile_pos = Matrix4f().translate(Vector3f(x * 2f, y * 2f, 0f))
        val target = Matrix4f()

        camera.getProjection().mul(world, target)
        target.mul(tile_pos)

        shader.setUniform("sampler", 0)
        shader.setUniform("projection", target)

        model.render()
    }

    fun addTex(name: String, tex: Texture) {
        if (!tile_textures.containsKey(name))
            tile_textures[name] = tex
        else
            error("Tile texture already added for name $name")
    }
}