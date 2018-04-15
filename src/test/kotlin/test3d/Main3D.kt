package test3d

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import thefireplace.gltest.io.Timer
import thefireplace.gltest.io.Window
import thefireplace.gltest.render.Shader
import thefireplace.gltest.render3d.Mesh

class Main3D {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main3D()
        }
    }

    init {
        Window.setCallbacks()
        //Initialize GLFW, and throw an exception if it fails.
        if (!glfwInit())
            throw IllegalStateException("Failed to initialize GLFW!")

        //Add window hints with info about the OpenGL version we want to use
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        val window = Window("LWJGL 3D Test Program")

        //Make the second OpenGL context (buffer)
        GL.createCapabilities()

        //Create the test mesh
        val testMesh = Mesh()
        testMesh.create(
                floatArrayOf(
                        -1f, -1f, 0f,
                        0f, 1f, 0f,
                        1f, -1f, 0f
                )
        )

        //Create the shader
        val shader = Shader("shader3d")

        val frame_cap = 1.0 / 60.0//Nummber of seconds divided by number of frames to display in that time.

        //Variables for calculating current FPS
        var frame_time = 0.0
        var frames = 0

        var time = Timer.getTime()
        var unprocessed = 0.0

        //Actions when the window is open.
        while (!window.shouldClose()) {
            //Beginning of the frame.

            var can_render = false

            //Get the current time
            val time_2 = Timer.getTime()
            //Calculate the time passed since the last frame
            val passed = time_2 - time
            unprocessed += passed
            frame_time += passed

            time = time_2

            //Anything that doesn't have to do with rendering goes in here.
            while (unprocessed >= frame_cap) {
                if (window.hasResized()) {
                    glViewport(0, 0, window.getWidth(), window.getHeight())
                }

                unprocessed -= frame_cap
                can_render = true

                //Make the window close if Escape is pressed.
                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE))
                    window.setShouldClose(true)

                //Poll for events (input, etc)
                window.update()

                if (frame_time >= 1.0) {
                    frame_time = 0.0
                    println("FPS: $frames")
                    frames = 0
                }
            }

            //Skip rendering if the frame couldn't possibly be any different than before.
            if (!can_render)
                continue

            frames++

            //Clear the buffer to black.
            glClear(GL_COLOR_BUFFER_BIT)

            //Draw stuff
            shader.useShader()
            testMesh.draw()

            //Switch between the buffers. The buffer that is being shown is the one that has been drawn on, and the hidden one is the one we are drawing to.
            window.swapBuffers()
        }

        //Destroy the mesh and shader
        testMesh.destroy()
        shader.destroy()

        //Clean up the memory that was used by the window
        glfwTerminate()
    }

}