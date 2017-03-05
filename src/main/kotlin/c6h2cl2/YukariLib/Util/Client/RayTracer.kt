package c6h2cl2.YukariLib.Util.Client

import c6h2cl2.YukariLib.Render.Cube
import c6h2cl2.YukariLib.Util.BlockPos
import c6h2cl2.YukariLib.Util.MathHelperEx
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK
import net.minecraft.util.Vec3
import net.minecraft.world.World

/**
 * @author kojin15.
 */
class RayTracer {

    private val pointer = Pointer3D.empty
    private val pointer2 = Pointer3D.empty

    private val s_pointer = Pointer3D.empty
    private var s_dist: Double = 0.0
    private var s_side: Int = 0
    private var c_cube: CubeIndexed? = null

    private fun traceSide(start: Pointer3D, end: Pointer3D, cube: Cube, side: Int) {
        pointer.set(start)
        val hit: Pointer3D? = when (side) {
            0 -> pointer.interZX(end, cube.getMinY())
            1 -> pointer.interZX(end, cube.getMaxY())
            2 -> pointer.interXY(end, cube.getMinZ())
            3 -> pointer.interXY(end, cube.getMaxZ())
            4 -> pointer.interYZ(end, cube.getMinX())
            5 -> pointer.interYZ(end, cube.getMaxX())
            else -> null
        }
        hit?:return

        when (side) {
            0, 1 -> if (!MathHelperEx.betweenIn(cube.getMinX(), hit.getX(), cube.getMaxX()) || !MathHelperEx.betweenIn(cube.getMinZ(), hit.getZ(), cube.getMaxZ())) return
            2, 3 -> if (!MathHelperEx.betweenIn(cube.getMinX(), hit.getX(), cube.getMaxX()) || !MathHelperEx.betweenIn(cube.getMinY(), hit.getY(), cube.getMaxY())) return
            4, 5 ->  if (!MathHelperEx.betweenIn(cube.getMinY(), hit.getY(), cube.getMaxY()) || !MathHelperEx.betweenIn(cube.getMinZ(), hit.getZ(), cube.getMaxZ())) return
        }

        val dist = pointer2.set(hit).delete(start).squared()
        if (dist < s_dist) {
            s_side = side
            s_dist = dist
            s_pointer.set(pointer)
        }
    }

    private fun rayTraceCube(start: Pointer3D, end: Pointer3D, cube: Cube): MovingObjectPosition? {
        s_dist = Double.MAX_VALUE
        s_side = -1

        for (i in 0..5) {
            traceSide(start, end, cube, i)
        }

        if (s_side < 0) return null

        val mop = MovingObjectPosition(0, 0, 0, s_side, s_pointer)
        mop.typeOfHit = null
        return mop
    }

    private fun rayTraceCubes(start: Pointer3D, end: Pointer3D, cubes: ArrayList<CubeIndexed>): MovingObjectPosition? {
        var c_dist = Double.MAX_VALUE
        var c_hit: MovingObjectPosition? = null

        cubes.forEach {
            var mop = rayTraceCube(start, end, it)
            if (mop != null && s_dist < c_dist) {
                mop = DataMOP(it.subHit, s_dist, mop)
                c_dist = s_dist
                c_hit = mop
                c_cube = it
            }
        }
        return c_hit
    }

    /**
     * block.collisionRayTrace で使う
     * @param start Pointer3D(startVec)
     * @param end Pointer3D(endVec)
     */
    fun rayTraceCubes(start: Pointer3D, end: Pointer3D, cubeList: ArrayList<CubeIndexed>, pos: BlockPos, block: Block?): MovingObjectPosition? {
        cubeList.forEach {
            it.add(pos)
        }
        val mop = rayTraceCubes(start, end, cubeList)
        if (mop != null) {
            mop.typeOfHit = BLOCK
            mop.blockX = pos.getX()
            mop.blockY = pos.getY()
            mop.blockZ = pos.getZ()
            if (block != null) c_cube!!.add(Pointer3D(-pos.getX(), -pos.getY(), -pos.getZ())).setBlockBounds(block)
        }
        return mop
    }

    class CubeIndexed(var subHit: Int, cube: Cube): Cube(cube)

    class DataMOP : MovingObjectPosition {

        var dist: Double

        constructor(data: Int, dist: Double, mop: MovingObjectPosition): super(0, 0, 0, 0, mop.hitVec) {
            typeOfHit = mop.typeOfHit
            blockX = mop.blockX
            blockY = mop.blockY
            blockZ = mop.blockZ
            sideHit = mop.sideHit
            subHit = data
            this.dist = dist
        }
    }

    companion object {
        val instance = RayTracer()

        fun blockRetrace(world: World, player: EntityPlayer, pos: BlockPos): MovingObjectPosition? {
            val block = pos.getBlockFromPos(world)
            val headVec = getCorrectedHeadVec(player)
            val lookVec = player.getLook(1.0f)
            val reach = getBlockReachDistance(player)
            val endVec = headVec.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach)
            return block.collisionRayTrace(world, pos.getX(), pos.getY(), pos.getZ(), headVec, endVec)
        }

        fun getCorrectedHeadVec(player: EntityPlayer): Vec3 {

            val v = Vec3.createVectorHelper(player.posX, player.posY, player.posZ)
            if (player.worldObj.isRemote) {
                v.yCoord += (player.getEyeHeight() - player.defaultEyeHeight).toDouble()// compatibility with eye height changing mods
            } else {
                v.yCoord += player.getEyeHeight().toDouble()
                if (player is EntityPlayerMP && player.isSneaking()) {
                    v.yCoord -= 0.08
                }
            }
            return v
        }

        fun getBlockReachDistance(player: EntityPlayer): Double {

            return if (player.worldObj.isRemote)
                getBlockReachDistance_client()
            else if (player is EntityPlayerMP) getBlockReachDistance_server(player) else 5.0
        }

        private fun getBlockReachDistance_server(player: EntityPlayerMP): Double {

            return player.theItemInWorldManager.blockReachDistance
        }

        @SideOnly(Side.CLIENT)
        private fun getBlockReachDistance_client(): Double {

            return Minecraft.getMinecraft().playerController.blockReachDistance.toDouble()
        }
    }
}