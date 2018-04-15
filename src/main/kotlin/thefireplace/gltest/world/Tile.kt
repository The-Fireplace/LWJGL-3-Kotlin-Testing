package thefireplace.gltest.world

class Tile(private val id:Int, private val texture:String) {

    fun getId():Int{
        return id
    }

    fun getTexture():String{
        return texture
    }
}