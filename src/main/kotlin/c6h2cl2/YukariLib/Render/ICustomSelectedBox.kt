package c6h2cl2.YukariLib.Render

import net.minecraft.entity.player.EntityPlayer

/**
 * @author kojin15.
 */
interface ICustomSelectedBox {
    /**
     * @param subHit Cubeと対応した値
     */
    fun getCustomSelectedBox(subHit: Int, player: EntityPlayer): CustomSelectedBox

    /**
     * @param subHit Cubeと対応した値
     * @return セレクトボックスを描画するかどうか
     */
    fun shouldRenderBox(subHit: Int, player: EntityPlayer): Boolean
}