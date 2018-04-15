package thefireplace.gltest.world

object TileRegistry {
    private val tiles = HashMap<Int, Tile>()

    fun registerTile(tile: Tile) {
        tiles[tile.getId()] = tile
    }

    fun hasTile(id: Int): Boolean {
        return tiles.containsKey(id)
    }

    fun getTile(id: Int): Tile? {
        return tiles[id]
    }

    fun getTileCount(): Int {
        return tiles.size
    }
}