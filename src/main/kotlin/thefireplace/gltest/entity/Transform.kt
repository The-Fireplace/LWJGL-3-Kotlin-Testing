package thefireplace.gltest.entity

import org.joml.Matrix4f
import org.joml.Vector3f

class Transform {
    var pos = Vector3f()
    var scale = Vector3f(1f)

    fun getProjection(target:Matrix4f):Matrix4f{
        return target.scale(scale).translate(pos)
    }
}