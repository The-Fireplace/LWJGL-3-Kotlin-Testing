package thefireplace.gltest.io

object Timer {
    /**
     * Get system time in seconds.
     */
    fun getTime():Double{
        return System.nanoTime()/1000000000.0
    }
}