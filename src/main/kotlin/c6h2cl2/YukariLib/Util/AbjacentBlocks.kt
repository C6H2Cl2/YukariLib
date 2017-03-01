package c6h2cl2.YukariLib.Util

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import java.util.*

/**
 * @author kojin15.
 */
class AbjacentBlocks {

    @Transient var blockList: ArrayList<PosRange> = arrayListOf()
    @Transient var flags: Array<Boolean?>? = null
    var maxRange = 4
    var listSize: Int = 0
    var radius: Float = 0.0f
    var worldObj: World
    var startPos: BlockPos = BlockPos.Empty
    var anyMeta = true
    var targetBlock: Block = Blocks.air
    var targetMeta: Int = 0

    var size: Int = 0
    var size2: Int = 0

    var rPos: BlockPos = BlockPos.Empty

    constructor(world: World, pos: BlockPos, range: Int) {
        this.worldObj = world
        this.init(world, pos, range)
    }

    constructor(world: World, pos: BlockPos) : this(world, pos, 4)

    fun clear() {
        this.blockList.clear()
        this.flags = null
    }

    fun init(world: World, pos: BlockPos, range: Int) {
        this.worldObj = world
        this.startPos = pos
        this.setTarget(pos.getBlockFromPos(world), pos.getMetaFromPos(world))
        this.setRange(range)
    }

    fun setTarget(block: Block, meta: Int) {
        this.targetBlock = block
        this.targetMeta = meta
        this.anyMeta = meta == -1
    }

    fun setRange(range: Int) {
        this.setRange(range, 0.6F)
    }

    fun setRange(range: Int, addValue: Float) {
        this.maxRange = MathHelperEx.cut(1, range, 127)

        this.size = maxRange * 2 + 1
        this.size2 = size * size
        this.rPos = BlockPos(maxRange - startPos.getX(), maxRange - startPos.getY(), maxRange - startPos.getZ())

        this.listSize = size * size * size
        this.setCircleRadius(addValue)
        this.flags = arrayOfNulls<Boolean>(listSize)
    }

    fun setCircleRadius(addValue: Float): Unit {
        val value: Float = when {
            addValue < 0.0f -> 0.0f
            addValue >= 1.0f -> 0.9999f
            else -> addValue
        }
        this.radius = (maxRange + value) * (maxRange + value)
    }

    fun isConnectBlock(): Boolean = isBlockConnection(0)

    fun getAbjacentBlocksList(): ArrayList<PosRange> {
        isConnectBlock()
        return blockList
    }

    fun isSquareConnectBlock(): Boolean = isBlockConnection(1)

    fun getSquareBlocksList(): ArrayList<PosRange> {
        isSquareConnectBlock()
        return blockList
    }

    fun isCircleConnectBlock(): Boolean = isBlockConnection(2)

    fun getCircleBlocksList(): ArrayList<PosRange> {
        isCircleConnectBlock()
        return blockList
    }

    fun getBlocksList(): ArrayList<PosRange> = this.blockList

    fun isBlockConnection(mode: Int): Boolean {
        if (!checkChunkBlock(worldObj, startPos, maxRange)) return false
        if (!this.blockList.isEmpty()) this.blockList.clear()
        if (this.flags != null) this.flags = arrayOfNulls(listSize)
        var modeSize = when (mode) {
            0 -> (listSize * 0.17).toInt()
            2 -> (listSize * 0.53).toInt()
            else -> listSize
        }
        this.blockList = ArrayList<PosRange>(modeSize)
        return isBlockConnection(startPos, mode)
    }

    fun isBlockConnection(pos: BlockPos, mode: Int): Boolean {
        this.add(pos, 0)
        var i = 0
        var isEnd = false
        while (!isEnd && i < blockList.size) {
            val posRange = blockList[i]

            val px = posRange.getBlockPos().getX()
            val py = posRange.getBlockPos().getY()
            val pz = posRange.getBlockPos().getZ()
            val pr = posRange.getRange()
            if (!isEnd) isEnd = this.checkBlock(BlockPos(px + 1, py, pz), pr, mode)
            if (!isEnd) isEnd = this.checkBlock(BlockPos(px - 1, py, pz), pr, mode)
            if (!isEnd) isEnd = this.checkBlock(BlockPos(px, py, pz + 1), pr, mode)
            if (!isEnd) isEnd = this.checkBlock(BlockPos(px, py, pz - 1), pr, mode)
            if (!isEnd) isEnd = this.checkBlock(BlockPos(px, py + 1, pz), pr, mode)
            if (!isEnd) isEnd = this.checkBlock(BlockPos(px, py - 1, pz), pr, mode)

            ++i
        }
        return isEnd
    }

    fun checkBlock(pos: BlockPos, range: Int, mode: Int): Boolean {
        if (pos.getY() > 255 || pos.getY() < 0) return false
        if (this.isRange(pos, range, mode) && !this.contains(pos)) {
            val block = pos.getBlockFromPos(worldObj)
            val meta = pos.getMetaFromPos(worldObj)
            if (this.canAddBlock(pos, block, meta, range)) this.add(pos, range)
        }
        return false
    }

    fun index(pos: BlockPos): Int = (pos.getX() + rPos.getX()) * size2 + (pos.getY() + rPos.getY()) * size + (pos.getZ() + rPos.getZ())

    fun add(pos: BlockPos, range: Int) {
        blockList.add(PosRange(pos, range))
        this.flags!![index(pos)] = true
    }

    fun contains(pos: BlockPos): Boolean = this.flags!![index(pos)]!!

    fun isRange(pos: BlockPos, range: Int, mode: Int): Boolean {
        return when (mode) {
            0 -> range <= this.maxRange
            1 -> isRangeSquare(pos)
            2 -> isRangeCircle(pos)
            else -> false
        }
    }

    fun isRangeSquare(pos: BlockPos): Boolean {
        return MathHelperEx.absolute(pos.getX() - this.startPos.getX()) <= maxRange &&
                MathHelperEx.absolute(pos.getY() - this.startPos.getY()) <= maxRange &&
                MathHelperEx.absolute(pos.getZ() - this.startPos.getZ()) <= maxRange
    }

    fun isRangeCircle(pos: BlockPos): Boolean {
        val rpos = pos.minus(startPos)
        return rpos.getX() * rpos.getX() + rpos.getY() * rpos.getY() + rpos.getZ() * rpos.getZ() <= this.radius
    }

    fun canAddBlock(pos: BlockPos, block: Block, meta: Int, range: Int): Boolean {
        return block == this.targetBlock && (this.anyMeta || meta == this.targetMeta)
    }

    fun canHarvestBlock(player: EntityPlayer?): Boolean = canHarvestBlock(startPos, player)

    fun canHarvestBlock(pos: BlockPos, player: EntityPlayer?): Boolean {
        val block = pos.getBlockFromPos(worldObj)
        val meta = pos.getMetaFromPos(worldObj)
        return block.canHarvestBlock(player, meta)
    }

    fun allRemove() {
        allBreak(null, false, false)
    }

    fun addBreak(player: EntityPlayer?, isAutoCollect: Boolean) {
        allBreak(player, this.canHarvestBlock(player), isAutoCollect)
    }

    fun allBreak(player: EntityPlayer?, isItemDrop: Boolean, isAutoCollect: Boolean) {
        val itemList = ArrayList<ItemStack>()
        blockList.forEach {
            val meta = it.getBlockPos().getMetaFromPos(worldObj)
            if (isItemDrop) {

                val block = it.getBlockPos().getBlockFromPos(worldObj)
                if (isAutoCollect) {
                    var margeStack = false

                    for (i in block.getDrops(worldObj, it.getBlockPos().getX(), it.getBlockPos().getY(), it.getBlockPos().getZ(), meta, 0)) {
                        margeStack = false
                        for (item in itemList) {
                            if (isItemNbtEqual(item, i) && item.stackSize + i.stackSize <= item.maxStackSize) {
                                item.stackSize += i.stackSize
                                margeStack = true
                                break
                            }
                        }
                        if (!margeStack && i != null) itemList.add(i)
                    }
                } else {
                    block.dropBlockAsItem(worldObj, it.getBlockPos().getX(), it.getBlockPos().getY(), it.getBlockPos().getZ(), meta, 0)
                }
            }
            this.worldObj.getBlock(it.getBlockPos().getX(), it.getBlockPos().getY(), it.getBlockPos().getZ())
            Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(it.getBlockPos().getX(), it.getBlockPos().getY(), it.getBlockPos().getZ())
        }
        if (isAutoCollect && !itemList.isEmpty()) {
            if (player != null) {
                itemList.forEach {
                    dropItem(it, worldObj, player.posX.toInt(), player.posY.toInt(), player.posZ.toInt(), 0.2, 0.2, 10, 6000)
                }
            }
        }
    }

    fun checkChunkBlock(world: World, pos: BlockPos, range: Int): Boolean {
        return world.checkChunksExist(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range)
    }

    fun isItemNbtEqual(item1: ItemStack?, item2: ItemStack): Boolean {
        return item1 != null && ItemStack.areItemStackTagsEqual(item1, item2) && item1.isItemEqual(item2)
    }

    fun dropItem(stack: ItemStack, world: World, x: Int, y: Int, z: Int, width: Double, height: Double, delay: Int, age: Int) {
        val rand = Random()
        val entityitem = EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack)
        entityitem.motionX = rand.nextDouble() * width - width / 2.0
        entityitem.motionY = rand.nextDouble() * height
        entityitem.motionZ = rand.nextDouble() * width - width / 2.0
        entityitem.delayBeforeCanPickup = delay
        entityitem.age = 6000 - age
        world.spawnEntityInWorld(entityitem)
    }
}