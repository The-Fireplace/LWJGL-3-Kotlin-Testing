package thefireplace.gltest.render

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import java.nio.FloatBuffer

open class Model(vertices:FloatArray, tex_coords:FloatArray, indices:IntArray) {
    var draw_count:Int = indices.size
    var v_id:Int
    var t_id:Int

    var i_id:Int

    init{

        v_id = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, v_id)
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW)//Static is used for passing data once; Dynamic is for changing it later

        t_id = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, t_id)
        glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW)

        i_id = glGenBuffers()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id)

        val buffer = BufferUtils.createIntBuffer(indices.size)
        buffer.put(indices)
        buffer.flip()

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)

        //Unbind the buffers
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun render(){
        glEnableVertexAttribArray(0)//0 is the same number we used in Shader when using glBindAttribLocation
        glEnableVertexAttribArray(1)//1 is the number for the texture attribute.

        glBindBuffer(GL_ARRAY_BUFFER, v_id)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, t_id)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id)
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0)

        //Unbind the buffers
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
    }

    private fun createBuffer(array:FloatArray):FloatBuffer{
        val buffer = BufferUtils.createFloatBuffer(array.size)
        buffer.put(array)
        buffer.flip()
        return buffer
    }

    /**
     * This is to be called instead of finalize, if it's even necessary. Including for completeness.
     */
    fun destroy(){
        glDeleteBuffers(v_id)
        glDeleteBuffers(t_id)
        glDeleteBuffers(i_id)
    }
}