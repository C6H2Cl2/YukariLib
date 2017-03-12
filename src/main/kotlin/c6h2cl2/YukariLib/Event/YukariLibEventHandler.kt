@file:Suppress("UNUSED")
package c6h2cl2.YukariLib.Event

import c6h2cl2.YukariLib.Render.ICustomBoundingBox
import c6h2cl2.YukariLib.Render.ICustomSelectedBox
import c6h2cl2.YukariLib.Render.RenderCustomSelectedBox
import c6h2cl2.YukariLib.Util.BlockPos
import c6h2cl2.YukariLib.Util.Client.Pointer3D
import c6h2cl2.YukariLib.Util.RenderUtil
import c6h2cl2.YukariLib.YukariLibCore
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent

/**
 * @author C6H2Cl2
 */
class YukariLibEventHandler {
    //プレイヤー死亡時に座標表示
    @SubscribeEvent
    fun onPlayerDeath(event: LivingDeathEvent){
        if (!YukariLibCore.enableDeathLog) return
        val player = event.entityLiving as? EntityPlayer ?: return
        player.addChatComponentMessage(ChatComponentText("You died at ${player.posX}, ${player.posY}, ${player.posZ}"))
    }

    //セレクトボックスの表示
    @SubscribeEvent
    fun worldRenderEvent(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft().thePlayer
        val world = Minecraft.getMinecraft().theWorld
        val MOP = Minecraft.getMinecraft().objectMouseOver
        val pos = BlockPos(MOP.blockX, MOP.blockY, MOP.blockZ)

        if (player != null) {
            val itemStack: ItemStack? = player.currentEquippedItem
            val item: Item? = itemStack?.item
            if (item != null && item is ICustomBoundingBox) {
                val list = item.getBoundingBoxPos(itemStack, player, world, pos, MOP.sideHit, Pointer3D(MOP.hitVec))
                RenderUtil.renderBoundingBoxFromPos(list, world, player, event.partialTicks, item.getRGBA(), item.getLineWidth())
            }
            val block = pos.getBlockFromPos(world)
            if (block is ICustomSelectedBox && block.shouldRenderBox(MOP.subHit, player)) {
                RenderCustomSelectedBox().drawSelectionBox(player, MOP, event.partialTicks, block.getCustomSelectedBox(MOP.subHit, player))
            }
        }
    }
}