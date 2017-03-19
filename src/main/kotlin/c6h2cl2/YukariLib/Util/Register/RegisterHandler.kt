package c6h2cl2.YukariLib.Util.Register

import c6h2cl2.YukariLib.Item.MetaItemBlock
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author kojin15.
 */
class RegisterHandler(private val modId: String) {
    private val items = LinkedList<ItemParameter>().toMutableList()
    private val blocks = LinkedList<BlockParameter>().toMutableList()

    fun handle(event: FMLPreInitializationEvent) {
        items.sortBy { it.item.unlocalizedName }
        blocks.sortBy { it.block.unlocalizedName }

        items.forEach {
            GameRegistry.register(it.item, getLocation(it.path))
            if (event.side.isClient) {
                for (i in 0..it.maxId) {
                    ModelLoader.setCustomModelResourceLocation(it.item, i, ModelResourceLocation(getLocation(it.path + i), "inventory"))
                }
            }
        }
        blocks.forEach {
            GameRegistry.register(it.block, getLocation(it.path))
            val item = MetaItemBlock(it.block)
            GameRegistry.register(item, getLocation(it.path))
            if (event.side.isClient) {
                for (i in 0..it.maxId) {
                    ModelLoader.setCustomModelResourceLocation(item, i, ModelResourceLocation(getLocation(it.path + i), "inventory"))
                }
            }
        }

    }

    fun build(target: Any): RegisterHandler {
        val members = target::class.declaredMemberProperties
        members.forEach {
            val location = ((it.annotations.find { it is Location } as Location?)?.path) ?: "missing"
            val maxId = ((it.annotations.find { it is SubTypes } as SubTypes?)?.maxId) ?: 0
            val field = (@Suppress("UNCHECKED_CAST") (it as KProperty1<Any, Any?>))
            val value = if (!field.isAbstract && field.visibility == KVisibility.PUBLIC) field.get(target) else null
            if (value != null) {
                if (value is Item) items.add(ItemParameter(value, maxId, location))
                if (value is Block) blocks.add(BlockParameter(value, maxId, location))
            }
        }
        return this
    }

    private fun getLocation(path: String): ResourceLocation = ResourceLocation(modId.toLowerCase(), path)
    private class ItemParameter(val item: Item, val maxId: Int, val path: String)
    private class BlockParameter(val block: Block, val maxId: Int, val path: String)
}