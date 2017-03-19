package c6h2cl2.YukariLib.Util

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos

/**
 * @author C6H2Cl2
 */

class BlockPosEx(x: Int, y: Int, z: Int) : BlockPos(x, y, z) {
    fun writeToNBT(tagCompound: NBTTagCompound): NBTTagCompound = writeToNBT(tagCompound, "BlockPos")

    fun writeToNBT(tagCompound: NBTTagCompound, name: String): NBTTagCompound {
        val tag: NBTTagCompound = NBTTagCompound()
        tag.setInteger("x", x)
        tag.setInteger("y", y)
        tag.setInteger("z", z)
        tagCompound.setTag(name, tag)
        return tagCompound
    }

    fun readFromNBT(tagCompound: NBTTagCompound): BlockPosEx = readFromNBT(tagCompound, "BlockPos")

    fun readFromNBT(tagCompound: NBTTagCompound, name: String): BlockPosEx {
        val tag = tagCompound.getCompoundTag(name)
        return BlockPosEx(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"))
    }
}