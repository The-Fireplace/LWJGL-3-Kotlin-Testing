package thefireplace.gltest.render3d

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*

class Mesh {
    private var vertexArrayObject = 0
    private var vertexBufferObject = 0

    private var vertexCount = 0

    fun create(vertices:FloatArray):Boolean{
        vertexArrayObject = glGenVertexArrays()
        glBindVertexArray(vertexArrayObject)

        vertexBufferObject = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindVertexArray(0)

        vertexCount = vertices.size / 3

        return true
    }

    fun destroy(){
        glDeleteBuffers(vertexBufferObject)
        glDeleteVertexArrays(vertexArrayObject)
    }

    fun draw(){
        glBindVertexArray(vertexArrayObject)
        glEnableVertexAttribArray(0)

        glDrawArrays(GL_TRIANGLES, 0, vertexCount)

        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }
}