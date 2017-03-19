package c6h2cl2.YukariLib.Util

import c6h2cl2.YukariLib.IToolWithType
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * @author C6H2Cl2
 */
object RecipeUtil {
    fun addToolRecipe(tool: IToolWithType, toolHead: ItemStack, toolRod: ItemStack) {
        val recipe = tool.getToolType().recipe
        if (tool is Item) {
            GameRegistry.addRecipe(ItemStack(tool), recipe[0], recipe[1], recipe[2], 'H', toolHead, 'R', toolRod)
        } else if (tool is Block) {
            GameRegistry.addRecipe(ItemStack(tool), recipe[0], recipe[1], recipe[2], 'H', toolHead, 'R', toolRod)
        } else {
            throw IllegalArgumentException()
        }
    }

    fun addToolRecipe(tool: IToolWithType, toolHead: Item, toolRod: Item) {
        addToolRecipe(tool, ItemStack(toolHead), ItemStack(toolRod))
    }

    fun addToolRecipe(tool: IToolWithType, toolHead: Item, toolRod: Block) {
        addToolRecipe(tool, ItemStack(toolHead), ItemStack(toolRod))
    }

    fun addToolRecipe(tool: IToolWithType, toolHead: Block, toolRod: Item) {
        addToolRecipe(tool, ItemStack(toolHead), ItemStack(toolRod))
    }

    fun addToolRecipe(tool: IToolWithType, toolHead: Block, toolRod: Block) {
        addToolRecipe(tool, ItemStack(toolHead), ItemStack(toolRod))
    }

    fun addToolRecipeInArray(results: Array<IToolWithType>, toolHeads: Array<ItemStack>, toolRods: Array<ItemStack>) {
        for (i in 0 until results.size) {
            addToolRecipe(results[i], toolHeads[i], toolRods[i])
        }
    }

    fun addToolRecipeInArray(results: Array<IToolWithType>, toolHeads: Array<Item>, toolRods: Array<Item>) {
        for (i in 0 until results.size) {
            addToolRecipe(results[i], toolHeads[i], toolRods[i])
        }
    }

    fun addToolRecipeInArray(results: Array<IToolWithType>, toolHeads: Array<Item>, toolRods: Array<Block>) {
        for (i in 0 until results.size) {
            addToolRecipe(results[i], toolHeads[i], toolRods[i])
        }
    }

    fun addToolRecipeInArray(results: Array<IToolWithType>, toolHeads: Array<Block>, toolRods: Array<Item>) {
        for (i in 0 until results.size) {
            addToolRecipe(results[i], toolHeads[i], toolRods[i])
        }
    }

    fun addToolRecipeInArray(results: Array<IToolWithType>, toolHeads: Array<Block>, toolRods: Array<Block>) {
        for (i in 0 until results.size) {
            addToolRecipe(results[i], toolHeads[i], toolRods[i])
        }
    }

    fun addCompressRecipe(result: ItemStack, material: ItemStack) {
        GameRegistry.addRecipe(result, "MMM", "MMM", "MMM", 'M', material)
    }

    fun addCompressRecipe(result: ItemStack, material: Item) {
        addCompressRecipe(result, ItemStack(material))
    }

    fun addCompressRecipe(result: ItemStack, material: Block) {
        addCompressRecipe(result, ItemStack(material))
    }
}