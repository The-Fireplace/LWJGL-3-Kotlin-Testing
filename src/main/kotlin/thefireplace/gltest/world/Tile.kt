package thefireplace.gltest.world

class Tile(private val texture: String) {

    private val id = globalId++

    companion object{
        var globalId = 0
    }

    fun getId(): Int {
        return id
    }

    fun getTexture(): String {
        return texture
    }
}