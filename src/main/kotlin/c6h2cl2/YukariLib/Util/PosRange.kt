package c6h2cl2.YukariLib.Util

/**
 * @author kojin15.
 */
class PosRange(private var x: Int, private var y: Int, private var z: Int, private var range: Int) {

    constructor(x: Int, y: Int, z: Int): this(x, y, z, 0)
    constructor(pos: BlockPos): this(pos.getX(), pos.getY(), pos.getZ())
    constructor(pos: BlockPos, range: Int): this(pos.getX(), pos.getY(), pos.getZ(), range)

    override fun equals(other: Any?): Boolean {
        if (other is PosRange) {
            return this.x == other.x && this.y == other.y && this.z == other.z
        } else return false
    }

    fun getBlockPos(): BlockPos = BlockPos(x, y, z)

    fun getRange(): Int = range
}