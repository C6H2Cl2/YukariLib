package c6h2cl2.YukariLib.Block

import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import kotlin.reflect.KClass

/**
 * @author C6H2Cl2
 */
class BlockWithTileEntity(material: Material, val tileClass: KClass<TileEntity>, val createTile: (World, Int) -> TileEntity, private var tileID: String = "") : CSBoxBlockContainer(material) {
    override fun createNewTileEntity(world: World, meta: Int): TileEntity {
        return createTile(world, meta)
    }

    fun setTileId(id: String) {
        if (tileID == "") tileID = id
    }

    fun getTileId(): String {
        return if (tileID == "") {
            unlocalizedName
        } else {
            tileID
        }
    }
}