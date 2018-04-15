package thefireplace.gltest.entity

import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import thefireplace.gltest.io.Window
import thefireplace.gltest.render.Camera
import thefireplace.gltest.world.World

class Player(textureName: String, transform: Transform): Entity(textureName, transform) {
    override fun update(delta:Float, window: Window, camera: Camera, world: World){
        //Move left if 'A' is down.
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A))
            transform.pos.add(Vector3f(delta*-2f, 0f, 0f))
        //Move up if 'W' is down.
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W))
            transform.pos.add(Vector3f(0f, delta*2f, 0f))
        //Move down if 'S' is down.
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S))
            transform.pos.add(Vector3f(0f, delta*-2f, 0f))
        //Move right if 'D' is down.
        if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D))
            transform.pos.add(Vector3f(delta*2f, 0f, 0f))

        super.update(delta, window, camera, world)
    }
}