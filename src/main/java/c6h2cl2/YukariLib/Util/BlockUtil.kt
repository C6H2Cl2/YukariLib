package c6h2cl2.YukariLib.Util

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.util.ResourceLocation

/**
 * @author C6H2Cl2
 */
class BlockUtil {
    companion object {
        fun CreateBlock(
                name: String, textureName: String = name, modID: String, material: Material, creativeTabs: CreativeTabs? = null, hardness: Float = 1.5f, resistance: Float = 10f,
                lightLevel: Float = 0f, harvestTool:String? = null, harvestLevel: Int = 0
        ): Block {
            val block = Block(material)
            block.unlocalizedName = name
            block.registryName = ResourceLocation(modID, textureName)
            block.setHardness(hardness)
            block.setResistance(resistance)
            if (creativeTabs != null) block.setCreativeTab(creativeTabs)
            if (harvestTool != null){
                block.setHarvestLevel(harvestTool, harvestLevel)
            }
            return block
        }
    }
}