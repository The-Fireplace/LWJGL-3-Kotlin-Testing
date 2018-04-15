package thefireplace.gltest.render

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Shader(filename: String) {
    var program: Int = glCreateProgram()
    var vs: Int
    var fs: Int

    val vertex_shader_ext = "vs"
    val fragment_shader_ext = "fs"

    init {

        vs = glCreateShader(GL_VERTEX_SHADER)
        glShaderSource(vs, readSource("$filename.$vertex_shader_ext"))
        glCompileShader(vs)
        //Ensure that the shader compiled successfully
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            error(glGetShaderInfoLog(vs))
            System.exit(1)
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER)
        glShaderSource(fs, readSource("$filename.$fragment_shader_ext"))
        glCompileShader(fs)
        //Ensure that the shader compiled successfully.
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            error(glGetShaderInfoLog(fs))
            System.exit(1)
        }

        glAttachShader(program, vs)
        glAttachShader(program, fs)

        glBindAttribLocation(program, 0, "vertices")//Use the same name for the attribute in the vertex shader file. Attribute is not used in fragment shader file.
        glBindAttribLocation(program, 1, "textures")

        glLinkProgram(program)
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            error(glGetProgramInfoLog(program))
            System.exit(1)
        }
        glValidateProgram(program)
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            error(glGetProgramInfoLog(program))
            System.exit(1)
        }
    }

    fun setUniform(name: String, value: Int) {
        val location = glGetUniformLocation(program, name)
        //Check if location is valid and set the value to be uniform.
        if (location != -1)
            glUniform1i(location, value)
    }

    fun setUniform(name: String, value: Matrix4f) {
        val location = glGetUniformLocation(program, name)
        val buffer = BufferUtils.createFloatBuffer(16)//16 because matrix4f is 4x4
        value.get(buffer)
        //Check if location is valid and set the value to be uniform.
        if (location != -1)
            glUniformMatrix4fv(location, false, buffer)
    }

    fun useShader() {
        glUseProgram(program)
    }

    fun destroy() {
        glDetachShader(program, vs)
        glDetachShader(program, fs)
        glDeleteShader(vs)
        glDeleteShader(fs)
        glDeleteProgram(program)
    }

    private fun readSource(filename: String): String {
        val string = StringBuilder()
        val br: BufferedReader
        try {
            br = BufferedReader(InputStreamReader(this::class.java.getResourceAsStream("/shaders/$filename")))
            br.forEachLine {
                string.append(it)
                string.append("\n")
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return string.toString()
    }
}