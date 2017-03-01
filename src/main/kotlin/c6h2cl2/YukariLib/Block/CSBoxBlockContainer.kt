package c6h2cl2.YukariLib.Block

import c6h2cl2.YukariLib.Render.CustomSelectedBox
import c6h2cl2.YukariLib.Render.ICustomSelectedBox
import c6h2cl2.YukariLib.Util.BlockPos
import c6h2cl2.YukariLib.Util.Pointer3D
import c6h2cl2.YukariLib.Util.RayTracer
import c6h2cl2.YukariLib.Util.RayTracer.CubeIndexed
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraft.world.World

/**
 * @author kojin15.
 */
abstract class CSBoxBlockContainer(material: Material) : BlockContainer(material), ICustomSelectedBox {

    override final fun onBlockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        world?:return false
        player?:return false
        val blockPos = BlockPos(x, y, z)
        val hitPos = Pointer3D(hitX, hitY, hitZ)
        val mop = Minecraft.getMinecraft().objectMouseOver
        return this.onBlockActivated(world, blockPos, player, hitPos, side, mop.subHit)
    }

    override final fun collisionRayTrace(world: World?, x: Int, y: Int, z: Int, startVec: Vec3?, endVec: Vec3?): MovingObjectPosition? {
        world?:return null
        val blockPos = BlockPos(x, y, z)
        val rayTracer = RayTracer.instance
        val cubeList = ArrayList<CubeIndexed>()
        return rayTracer.rayTraceCubes(Pointer3D(startVec!!), Pointer3D(endVec!!), this.collisionRayTrace(world, blockPos, cubeList, rayTracer), blockPos, this)
    }

    open fun onBlockActivated(world: World, blockPos: BlockPos, player: EntityPlayer, hitPos: Pointer3D, side: Int, subHit: Int): Boolean = false

    open fun collisionRayTrace(world: World, blockPos: BlockPos, cubeList: ArrayList<CubeIndexed>, rayTracer: RayTracer): ArrayList<CubeIndexed> = cubeList

    override fun shouldRenderBox(subHit: Int, player: EntityPlayer): Boolean = false

    override fun getCustomSelectedBox(subHit: Int, player: EntityPlayer): CustomSelectedBox = CustomSelectedBox(0.5, 0.5, 0.5, 1.0, 1.0, 1.0)


}