package thefireplace.gltest.render2d

import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(width: Int, height: Int) {
    private var position: Vector3f
    private var projection: Matrix4f = Matrix4f()

    init {
        position = Vector3f(0f, 0f, 0f)
        setProjection(width, height)
    }

    fun setPosition(position: Vector3f) {
        this.position = position
    }

    fun addPosition(position: Vector3f) {
        this.position.add(position)
    }

    fun getPosition(): Vector3f {
        return position
    }

    fun getProjection(): Matrix4f {
        return projection.translate(position, Matrix4f())
    }

    fun setProjection(width: Int, height: Int) {
        projection = Matrix4f().setOrtho2D(-width / 2f, width / 2f, -height / 2f, height / 2f)//ortho2D sets the origin of the graphics, in this case it is set to the center of the screen
    }
}