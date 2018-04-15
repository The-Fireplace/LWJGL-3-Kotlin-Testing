package thefireplace.gltest.io

import org.lwjgl.glfw.GLFW.*

class Input(private val window:Long) {

    val keys = BooleanArray(GLFW_KEY_LAST, {false})

    /**
     * @return true if the key is down.
     */
    fun isKeyDown(key:Int):Boolean{
        return glfwGetKey(window, key) == GLFW_TRUE
    }

    /**
     * Returns true if the key is pressed and it wasn't during the previous frame (true only when the key is first pressed)
     */
    fun isKeyPressed(key:Int):Boolean{
        return isKeyDown(key) && !keys[key]
    }

    /**
     * Returns true if the key is not pressed and it was during the previous frame (true only when the key is first released)
     */
    fun isKeyReleased(key:Int):Boolean{
        return !isKeyDown(key) && keys[key]
    }

    /**
     * @return true if the mouse button is down.
     */
    fun isMouseButtonDown(button:Int):Boolean{
        return glfwGetMouseButton(window, button) == GLFW_TRUE
    }

    fun update(){
        for(i in 32 until keys.size)
            keys[i] = isKeyDown(i)
    }
}