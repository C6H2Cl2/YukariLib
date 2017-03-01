package c6h2cl2.YukariLib.Render

import c6h2cl2.YukariLib.Util.BlockPos
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11

/**
 * @author kojin15.
 */
@SideOnly(Side.CLIENT)
class RenderCustomSelectedBox {

    val extraSpace = 0.002

    fun drawSelectionBox(thePlayer: EntityPlayer, mop: MovingObjectPosition, pTickTime: Float, selectedBox: CustomSelectedBox) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            GL11.glEnable(GL11.GL_BLEND)
            OpenGlHelper.glBlendFunc(770, 771, 1, 0)
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.4f)
            GL11.glLineWidth(2.0f)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDepthMask(false)
            val block = thePlayer.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ)

            if (block.material !== Material.air) {
                block.setBlockBoundsBasedOnState(thePlayer.worldObj, mop.blockX, mop.blockY, mop.blockZ)
                val xTick = thePlayer.lastTickPosX + (thePlayer.posX - thePlayer.lastTickPosX) * pTickTime
                val yTick = thePlayer.lastTickPosY + (thePlayer.posY - thePlayer.lastTickPosY) * pTickTime
                val zTick = thePlayer.lastTickPosZ + (thePlayer.posZ - thePlayer.lastTickPosZ) * pTickTime
                drawOutlinedBoundingBox(selectedBox.addExtraSpace(extraSpace).offsetForDraw(-xTick, -yTick, -zTick))
            }

            GL11.glDepthMask(true)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
        }
    }

    fun drawOutlinedBoundingBox(selectedBox: CustomSelectedBox) {
        val tessellator = Tessellator.instance
        tessellator.startDrawing(1)

        //上下
        addMainVertex(selectedBox, 1, selectedBox.middleHeight, tessellator)
        addMainVertex(selectedBox, 0, 0.0, tessellator)

        addTopBottomVertex(selectedBox, 1, selectedBox.sideLength[1], selectedBox.middleHeight + selectedBox.sideLength[1], tessellator)
        addTopBottomVertex(selectedBox, 0, -selectedBox.sideLength[0], -selectedBox.sideLength[0], tessellator)

        //縦の線
        addVerticalVertexs(selectedBox, tessellator)

        tessellator.draw()
    }

    fun addVerticalVertexs(selectedBox: CustomSelectedBox, tessellator: Tessellator) {
        if (selectedBox.drawSide[2]) {
            tessellator.addVertex(selectedBox.minX, selectedBox.minY, selectedBox.minZ - selectedBox.sideLength[2])
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ - selectedBox.sideLength[2])
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY, selectedBox.minZ - selectedBox.sideLength[2])
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ - selectedBox.sideLength[2])
        }
        if (selectedBox.drawSide[3]) {
            tessellator.addVertex(selectedBox.minX, selectedBox.minY, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
        }
        if (selectedBox.drawSide[4]) {
            tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ + selectedBox.middleWidth)
        }
        if (selectedBox.drawSide[5]) {
            tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ + selectedBox.middleWidth)
        }
        if (!selectedBox.drawSide[2] && !selectedBox.drawSide[5]) {
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ)
        }
        if (!selectedBox.drawSide[3] && !selectedBox.drawSide[5]) {
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ + selectedBox.middleWidth)
        }
        if (!selectedBox.drawSide[2] && !selectedBox.drawSide[4]) {
            tessellator.addVertex(selectedBox.minX, selectedBox.minY, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ)
        }
        if (!selectedBox.drawSide[3] && !selectedBox.drawSide[4]) {
            tessellator.addVertex(selectedBox.minX, selectedBox.minY, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + selectedBox.middleHeight, selectedBox.minZ + selectedBox.middleWidth)
        }

    }

    fun addTopBottomVertex(selectedBox: CustomSelectedBox, side: Int, sideLength: Double, heightToAdd: Double, tessellator: Tessellator) {
        if (selectedBox.drawSide[side]) {
            //四角形
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

            //縦の線
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd - sideLength, selectedBox.minZ)

            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd - sideLength, selectedBox.minZ)

            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd - sideLength, selectedBox.minZ + selectedBox.middleWidth)

            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd - sideLength, selectedBox.minZ + selectedBox.middleWidth)

        }
    }

    fun addMainVertex(selectedBox: CustomSelectedBox, side: Int, heightToAdd: Double, tessellator: Tessellator) {
        if (!selectedBox.drawSide[side]) {
            if (!selectedBox.drawSide[4]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            } else {
                //メインの線
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

                //北側
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)

                //南側
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            }
            if (!selectedBox.drawSide[5]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            } else {
                //メインの線
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth + selectedBox.sideLength[5], selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth + selectedBox.sideLength[5], selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

                //北側
                tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)

                //南側
                tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + +selectedBox.middleWidth)
            }

            if (!selectedBox.drawSide[2]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
            } else {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])

                //北側
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)

                //南側
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)

            }
            if (!selectedBox.drawSide[3]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            } else {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)

                //北側
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

                //南側
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            }
        } else {
            if (selectedBox.drawSide[4]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

                //北側
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)

                //南側
                tessellator.addVertex(selectedBox.minX - selectedBox.sideLength[4], selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            }
            if (selectedBox.drawSide[5]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth + selectedBox.sideLength[5], selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth + selectedBox.sideLength[5], selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

                //北側
                tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)

                //南側
                tessellator.addVertex(selectedBox.minX + selectedBox.sideLength[5] + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + +selectedBox.middleWidth)
            }

            if (selectedBox.drawSide[2]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])

                //北側
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ)

                //南側
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ - selectedBox.sideLength[2])
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ)

            }
            if (selectedBox.drawSide[3]) {
                //メインの線
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)

                //北側
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)

                //南側
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.sideLength[3] + selectedBox.middleWidth)
                tessellator.addVertex(selectedBox.minX + selectedBox.middleDepth, selectedBox.minY + heightToAdd, selectedBox.minZ + selectedBox.middleWidth)
            }
        }
    }
}