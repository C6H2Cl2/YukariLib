package c6h2cl2.YukariLib.Util

import c6h2cl2.YukariLib.Block.SimpleBlock
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

/**
 * @author C6H2Cl2
 */
class BlockUtil {
    companion object{
        fun CreateBlock(name: String, textureName: String = name, modID: String, material:Material,creativeTabs: CreativeTabs? = null , hardness:Float=1.5f, resistance:Float=10f):Block{
            val block = SimpleBlock(material)
            block.setBlockName(name)
            block.setBlockTextureName(modID.toLowerCase()+":"+textureName)
            block.setHardness(hardness)
            block.setResistance(resistance)
            if(creativeTabs != null)block.setCreativeTab(creativeTabs)
            return block
        }
    }
}