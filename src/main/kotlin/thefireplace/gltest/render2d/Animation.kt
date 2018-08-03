package thefireplace.gltest.render2d

import thefireplace.gltest.io.Timer

class Animation(amount: Int, fps: Int, filename: String) {
    private val frames: Array<Texture?> = Array(amount){null}
    private var texturePointer: Int = 0

    private var elapsedTime: Double = 0.0
    private var currentTime: Double = 0.0
    private var lastTime: Double = Timer.getTime()
    private val fps: Double = 1.0 / fps

    init{
        for (i in 0 until amount)
            this.frames[i] = Texture("$filename-$i.png")
    }

    fun bind() {
        bind(0)
    }

    fun bind(sampler: Int) {
        this.currentTime = Timer.getTime()
        this.elapsedTime += currentTime - lastTime

        if (elapsedTime >= fps) {
            elapsedTime = 0.0
            texturePointer++
        }

        if (texturePointer >= frames.size) texturePointer = 0

        this.lastTime = currentTime

        frames[texturePointer]?.bind(sampler)
    }
}