package c6h2cl2.YukariLib.Util

import net.minecraft.block.Block
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection.*
import net.minecraftforge.common.util.ForgeDirection
import kotlin.comparisons.maxOf
import kotlin.comparisons.minOf

/**
 * @author C6H2Cl2
 */
data class BlockPos(private var x: Int, private var y: Int, private var z: Int) {
    companion object {
        @JvmStatic
        val Empty = BlockPos(0, 0, 0)
        @JvmStatic
        val Max_Pos = BlockPos(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        @JvmStatic
        val Min_Pos = BlockPos(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
    }

    constructor(nbtTagCompound: NBTTagCompound, name: String = "blockPos") : this(0, 0, 0) {
        readFromNBT(nbtTagCompound, name)
    }

    // Directions
    val up: BlockPos
        get() = BlockPos(x, y + 1, z)

    val down: BlockPos
        get() = BlockPos(x, y - 1, z)

    val north: BlockPos
        get() = BlockPos(x, y, z - 1)

    val south: BlockPos
        get() = BlockPos(x, y, z + 1)

    val east: BlockPos
        get() = BlockPos(x + 1, y, z)

    val west: BlockPos
        get() = BlockPos(x - 1, y, z)

    fun up(value: Int) = BlockPos(x, y + value, z)

    fun down(value: Int) = BlockPos(x, y - value, z)

    fun north(value: Int) = BlockPos(x, y, z - value)

    fun south(value: Int) = BlockPos(x, y, z + value)

    fun east(value: Int) = BlockPos(x + value, y, z)

    fun west(value: Int) = BlockPos(x - value, y, z)

    //Utils
    fun getTileEntityFromPos(world: IBlockAccess) = world.getTileEntity(x, y, z)

    fun getBlockFromPos(world: World): Block = world.getBlock(x, y, z)

    fun getMetaFromPos(world: World): Int = world.getBlockMetadata(x, y, z)

    fun getDistance(posFrom: BlockPos, posTo: BlockPos): Double {
        return Math.sqrt(Math.pow((posFrom.x - posTo.x).toDouble(), 2.0) + Math.pow((posFrom.y - posTo.y).toDouble(), 2.0) + Math.pow((posFrom.z - posTo.z).toDouble(), 2.0))
    }

    fun getDistance(pos: BlockPos): Double = getDistance(this, pos)

    fun getBlockDirection(tilePos: BlockPos): ForgeDirection {
        if (up == tilePos) {
            return ForgeDirection.UP
        } else if (down == tilePos) {
            return ForgeDirection.DOWN
        } else if (north == tilePos) {
            return ForgeDirection.NORTH
        } else if (south == tilePos) {
            return ForgeDirection.SOUTH
        } else if (east == tilePos) {
            return ForgeDirection.EAST
        } else if (west == tilePos) {
            return ForgeDirection.WEST
        } else {
            return ForgeDirection.UNKNOWN
        }
    }

    fun getPosForDirection(direction: ForgeDirection): BlockPos {
        return when (direction) {
            DOWN -> down
            UP -> up
            NORTH -> north
            SOUTH -> south
            EAST -> east
            WEST -> west
            UNKNOWN -> this
        }
    }

    fun searchBlock(pos: BlockPos, block: Block, world: World): List<BlockPos> {
        val targets = arrayListOf<BlockPos>()
        return rangeTo(pos)
                .filter {
                    val b = it.getBlockFromPos(world)
                    b == block || b === block
                }
    }

    //NBT
    @JvmOverloads fun writeToNBT(tagCompound: NBTTagCompound, tagName: String = "blockPos"): NBTTagCompound {
        val tag = NBTTagCompound()
        tag.setInteger("x", x)
        tag.setInteger("y", y)
        tag.setInteger("z", z)
        tagCompound.setTag(tagName, tag)
        return tagCompound
    }

    @JvmOverloads fun readFromNBT(tagCompound: NBTTagCompound, tagName: String = "blockPos"): BlockPos {
        val tag = tagCompound.getCompoundTag(tagName)
        x = tag.getInteger("x")
        y = tag.getInteger("y")
        z = tag.getInteger("z")
        return this
    }

    //Getter
    fun getX() = x

    fun getY() = y
    fun getZ() = z

    //Operators
    operator fun plus(pos: BlockPos) = BlockPos(x + pos.x, y + pos.y, z + pos.z)

    operator fun plusAssign(pos: BlockPos) {
        this.x += pos.x
        this.y += pos.y
        this.z += pos.z
    }

    operator fun minus(pos: BlockPos) = BlockPos(x - pos.x, y - pos.y, z - pos.z)

    operator fun minusAssign(pos: BlockPos) {
        this.x -= pos.x
        this.y -= pos.y
        this.z -= pos.z
    }

    operator fun times(value: Int) = BlockPos(x * value, y * value, z * value)

    operator fun timesAssign(value: Int) {
        x *= value
        y *= value
        z *= value
    }

    operator fun rangeTo(toPos: BlockPos): List<BlockPos> {
        val list = ArrayList<BlockPos>().toMutableList()
        for (i in minOf(x, toPos.x)..maxOf(x, toPos.x)) {
            for (j in minOf(y, toPos.y)..maxOf(y, toPos.y)) {
                (minOf(z, toPos.z)..maxOf(z, toPos.z)).mapTo(list) { BlockPos(i, j, it) }
            }
        }
        return list.toList()
    }

    operator fun get(type: Char): Int {
        return when (type) {
            'x' -> x
            'y' -> y
            'z' -> z
            else -> 0
        }
    }

    operator fun get(direction: ForgeDirection): BlockPos {
        return when (direction) {
            UP -> up
            DOWN -> down
            NORTH -> north
            SOUTH -> south
            EAST -> east
            WEST -> west
            else -> Empty
        }
    }

    operator fun get(direction: ForgeDirection, range: Int): BlockPos {
        return when (direction) {
            UP -> up(range)
            DOWN -> down(range)
            NORTH -> north(range)
            SOUTH -> south(range)
            EAST -> east(range)
            WEST -> west(range)
            else -> Empty
        }
    }
}