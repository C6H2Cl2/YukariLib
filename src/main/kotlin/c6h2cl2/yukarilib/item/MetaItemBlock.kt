package c6h2cl2.yukarilib.item

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

/**
 * @author kojin15.
 */
class MetaItemBlock(block: Block): ItemBlock(block) {
    override fun getMetadata(damage: Int): Int = damage
}