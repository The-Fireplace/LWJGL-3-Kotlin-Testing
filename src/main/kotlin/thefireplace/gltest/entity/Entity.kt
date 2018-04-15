package thefireplace.gltest.entity

import org.joml.Vector3f
import thefireplace.gltest.io.Window
import thefireplace.gltest.render.Camera
import thefireplace.gltest.render.Model
import thefireplace.gltest.render.Shader
import thefireplace.gltest.render.Texture
import thefireplace.gltest.world.World

open class Entity(textureName: String, protected val transform: Transform) {
    companion object {
        private var defaultEntityModel: Model? = null

        fun initModel() {
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
            defaultEntityModel = Model(vertices, texture, indices)
        }

        fun delModel() {
            defaultEntityModel = null
        }
    }

    private val texture = Texture(textureName)

    open fun update(delta: Float, window: Window, camera: Camera, world: World) {
        camera.setPosition(transform.pos.mul(-world.scale, Vector3f()))
    }

    open fun render(shader: Shader, camera: Camera, world: World) {
        val model = getModel() ?: return
        val target = camera.getProjection()
        target.mul(world.world)
        shader.bind()
        shader.setUniform("sampler", 0)
        shader.setUniform("projection", transform.getProjection(target))
        texture.bind(0)
        model.render()
    }

    open fun getModel(): Model? {
        return defaultEntityModel
    }
}