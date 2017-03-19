package c6h2cl2.YukariLib

import c6h2cl2.YukariLib.Block.SimpleBlock
import c6h2cl2.YukariLib.Util.Register.Location
import c6h2cl2.YukariLib.Util.Register.SubTypes
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

/**
 * @author kojin15.
 */
object Register {

    @Location("simpleBlock") @SubTypes(0)
    val simpleBlock: Block = SimpleBlock(Material.ROCK).setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
}