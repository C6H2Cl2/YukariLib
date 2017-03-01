package c6h2cl2.YukariLib.Util

import c6h2cl2.YukariLib.Util.BlockPos
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3

/**
 * @author kojin15.
 */
class Pointer3D(private var x: Double, private var y: Double, private var z: Double) {
    companion object {
        @JvmStatic
        val empty = Pointer3D(0.0, 0.0, 0.0)
        @JvmStatic
        val one = Pointer3D(1.0, 1.0, 1.0)
        @JvmStatic
        val center = Pointer3D(0.5, 0.5, 0.5)
    }

    fun getX(): Double = x
    fun getY(): Double = y
    fun getZ(): Double = z

    constructor(x: Int, y: Int, z: Int): this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float, y: Float, z: Float): this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(vec: Vec3): this(vec.xCoord, vec.yCoord, vec.zCoord)
    constructor(pos: BlockPos): this(pos.getX(), pos.getY(), pos.getZ())
    constructor(tagCompound: NBTTagCompound, tagName: String = "BlockPosD"): this(0.0, 0.0, 0.0) {
        this.readFromNBT(tagCompound, tagName)
    }

    fun copy(): Pointer3D = Pointer3D(x, y, z)

    fun squared(): Double = x * x + y * y + z * z

    fun vec3(): Vec3 = Vec3.createVectorHelper(x, y, z)

    fun add(posD: Pointer3D): Pointer3D {
        this.x += posD.x
        this.y += posD.y
        this.z += posD.z
        return this
    }

    fun add(x: Double, y: Double, z: Double): Pointer3D {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun delete(posD: Pointer3D): Pointer3D {
        this.x -= posD.x
        this.y -= posD.y
        this.z -= posD.z
        return this
    }

    fun delete(x: Double, y: Double, z: Double): Pointer3D {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun set(posD: Pointer3D): Pointer3D {
        this.x = posD.x
        this.y = posD.y
        this.z = posD.z
        return this
    }

    fun set(x: Double, y: Double, z: Double): Pointer3D {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun interXY(end: Pointer3D, paramZ: Double): Pointer3D? {
        val xd = end.x - this.x
        val yd = end.y - this.y
        val yz = end.z - this.z

        if (yz == 0.0) return null

        val d = (paramZ - this.z) / yz
        if (MathHelperEx.betweenIn(-1E-5, d, 1E-5)) return this
        if (!MathHelperEx.betweenIn(0.0, d, 1.0)) return null

        this.x += d * xd
        this.y += d * yd
        this.z = paramZ
        return this
    }

    fun interYZ(end: Pointer3D, paramX: Double): Pointer3D? {
        val xd = end.x - this.x
        val yd = end.y - this.y
        val zd = end.z - this.z

        if (xd == 0.0) return null

        val d = (paramX - x) / xd
        if (MathHelperEx.betweenIn(-1E-5, d, 1E-5)) return this
        if (!MathHelperEx.betweenIn(0.0, d, 1.0)) return null


        this.x = paramX
        this.y += d * yd
        this.z += d * zd
        return this
    }

    fun interZX(end: Pointer3D, paramY: Double): Pointer3D? {
        val xd = end.x - this.x
        val yd = end.y - this.y
        val zd = end.z - this.z

        if (yd == 0.0) return null

        val d = (paramY - y) / yd
        if (MathHelperEx.betweenIn(-1E-5, d, 1E-5)) return this
        if (!MathHelperEx.betweenIn(0.0, d, 1.0)) return null

        this.x += d * xd
        this.y = paramY
        this.z += d * zd
        return this
    }

    @JvmOverloads fun readFromNBT(tagCompound: NBTTagCompound, tagName: String = "BlockPosD"): Pointer3D {
        val tag: NBTTagCompound = tagCompound.getCompoundTag(tagName)
        x = tag.getDouble("x")
        y = tag.getDouble("y")
        z = tag.getDouble("z")
        return this
    }

    @JvmOverloads fun writeToNBT(tagCompound: NBTTagCompound, tagName: String = "BlockPosD"): NBTTagCompound {
        val tag = NBTTagCompound()
        tag.setDouble("x", x)
        tag.setDouble("y", y)
        tag.setDouble("z", z)
        tagCompound.setTag(tagName, tag)
        return tagCompound
    }
}