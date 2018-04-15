package thefireplace.gltest.render

import org.lwjgl.BufferUtils
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*

class Texture(filename:String) {
    private var id:Int = -1
    private var width:Int = 0
    private var height:Int = 0

    init {
        val bi:BufferedImage
        try{
            bi = ImageIO.read(this::class.java.getResourceAsStream("/$filename"))
            width = bi.width
            height = bi.height

            val pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width)

            val pixels = BufferUtils.createByteBuffer(width*height*4)
            for(j:Int in 0 until height)
                for(i:Int in 0 until width){
                    val pixel = pixels_raw[i*width + j]
                    pixels.put(((pixel shr 16) and 0xFF).toByte())//RED
                    pixels.put(((pixel shr 8) and 0xFF).toByte())//GREEN
                    pixels.put((pixel and 0xFF).toByte())//BLUE
                    pixels.put(((pixel shr 24) and 0xFF).toByte())//ALPHA
                }

            //Flip the buffer for OpenGL
            pixels.flip()

            //set the texture ID
            id = glGenTextures()

            //Bind the id as a 2D texture
            glBindTexture(GL_TEXTURE_2D, id)

            //Set the MIN and MAG texture filter. Tutorial said use either GL_NEAREST or GL_LINEAR, and GL_NEAREST looks cleaner.
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

            //Finish creating the texture.
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels)
        }catch(e: IOException){
            error("Could not find file: $filename")
            e.printStackTrace()
        }
    }

    /**
     * This is to be called instead of finalize, if it's even necessary. Including for completeness.
     */
    fun destroy(){
        glDeleteTextures(id)
    }

    fun bind(sampler:Int){
        if(width >= 0 && height >= 0) {
            if(sampler in 0..31) {
                glActiveTexture(GL_TEXTURE0 + sampler)
                glBindTexture(GL_TEXTURE_2D, id)
            }else
                error("Attempted to use invalid sampler.")
        }else
            error("Attempted to bind invalid texture.")
    }
}