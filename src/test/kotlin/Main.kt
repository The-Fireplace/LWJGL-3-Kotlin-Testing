import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import thefireplace.gltest.entity.Entity
import thefireplace.gltest.entity.Player
import thefireplace.gltest.entity.Transform
import thefireplace.gltest.io.Timer
import thefireplace.gltest.io.Window
import thefireplace.gltest.render.Camera
import thefireplace.gltest.render.Shader
import thefireplace.gltest.render.Texture
import thefireplace.gltest.render.TileRenderer
import thefireplace.gltest.world.Tile
import thefireplace.gltest.world.TileRegistry
import thefireplace.gltest.world.World

class Main {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }

    init {
        Window.setCallbacks()
        //Initialize GLFW, and throw an exception if it fails.
        if (!glfwInit())
            throw IllegalStateException("Failed to initialize GLFW!")

        val window = Window("LWJGL Test Program")

        //Make the second OpenGL context (buffer)
        GL.createCapabilities()

        //Create the camera
        val camera = Camera(window.getWidth(), window.getHeight())

        //Enable 2d textures
        glEnable(GL_TEXTURE_2D)

        //Create a Tile Renderer
        val tileRenderer = TileRenderer()

        //Create the shader
        val shader = Shader("shader")

        //Create tile textures and register them to our renderer. This must happen after GL.createCapabilities()
        tileRenderer.addTex("tiletex1", Texture("tile2.jpg"))
        tileRenderer.addTex("tiletex2", Texture("tile1.png"))

        var tileId = 0

        //Register tiles.
        TileRegistry.registerTile(Tile(tileId++, "tiletex1"))
        val tile2 = Tile(tileId, "tiletex2")
        TileRegistry.registerTile(tile2)

        //Create a world
        val world = World(25, 25, 16f)

        world.setTile(tile2, 1, 1)

        //Init entity base model
        Entity.initModel()

        //Create a player
        val player = Player("char.png", Transform())

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
                    camera.setProjection(window.getWidth(), window.getHeight())
                    glViewport(0, 0, window.getWidth(), window.getHeight())
                }

                unprocessed -= frame_cap
                can_render = true

                //Make the window close if Escape is pressed.
                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE))
                    window.setShouldClose(true)

                player.update(frame_cap.toFloat(), window, camera, world)

                //Correct camera position
                world.correctCamera(camera, window)

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

            //Render the world
            world.render(tileRenderer, shader, camera)

            //Render the player
            player.render(shader, camera, world)

            //Switch between the buffers. The buffer that is being shown is the one that has been drawn on, and the hidden one is the one we are drawing to.
            window.swapBuffers()
        }

        //Delete entity model
        Entity.delModel()

        //Clean up the memory that was used by the window
        glfwTerminate()
    }

}