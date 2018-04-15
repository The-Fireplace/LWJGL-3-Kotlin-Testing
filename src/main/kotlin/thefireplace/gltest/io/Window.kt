package thefireplace.gltest.io

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWWindowSizeCallback

class Window(val title: String, private var width: Int, private var height: Int, private var fullscreen: Boolean) {
    constructor(title: String) : this(title, 1280, 720, false)
    constructor(title: String, width: Int, height: Int) : this(title, width, height, false)

    //this is a pointer reference to window for glfw.
    private var window: Long = 0

    private var input: Input
    private var hasResized: Boolean
    private var windowSizeCallback: GLFWWindowSizeCallback? = null

    companion object {
        fun setCallbacks() {
            glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err))
        }
    }

    fun setLocalCallbacks() {
        windowSizeCallback = GLFWWindowSizeCallback.create { _, width, height ->
            run {
                this.width = width
                this.height = height
                hasResized = true
            }
        }
        glfwSetWindowSizeCallback(window, windowSizeCallback)
    }

    init {
        setSize(width, height)
        setFullscreen(fullscreen)
        createWindow()
        //Create the input for this window.
        input = Input(window)
        hasResized = false
    }

    private fun createWindow() {
        window = glfwCreateWindow(width, height, title, if (fullscreen) glfwGetPrimaryMonitor() else 0, 0)

        if (window == 0L)
            throw IllegalStateException("Failed to create window.")

        if (!fullscreen) {
            val vid = glfwGetVideoMode(glfwGetPrimaryMonitor())
            //Center the window
            if (vid != null)
                glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2)
            else
                error("GLFW video mode was null!")

            glfwShowWindow(window)
        }

        //Allows lwjgl to create capabilities
        glfwMakeContextCurrent(window)
        //init resize callback
        setLocalCallbacks()
    }

    fun update() {
        hasResized = false
        input.update()
        glfwPollEvents()
    }

    fun cleanup() {
        glfwFreeCallbacks(window)
    }

    fun setShouldClose(shouldClose: Boolean) {
        glfwSetWindowShouldClose(window, shouldClose)
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(window)
    }

    fun swapBuffers() {
        glfwSwapBuffers(window)
    }

    fun setFullscreen(fullscreen: Boolean) {
        this.fullscreen = fullscreen
    }

    fun isFullscreen(): Boolean {
        return fullscreen
    }

    fun getInput(): Input {
        return input
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }

    fun hasResized(): Boolean {
        return hasResized
    }
}