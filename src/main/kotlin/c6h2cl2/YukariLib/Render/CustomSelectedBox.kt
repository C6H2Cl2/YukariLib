package c6h2cl2.YukariLib.Render

/**
 * @author kojin15.
 */
class CustomSelectedBox {

    var drawSide = booleanArrayOf(false, false, false, false, false, false)
    var sideLength = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    var middleHeight = 0.0
    var middleWidth = 0.0
    var middleDepth = 0.0
    var minX = 0.0
    var minY = 0.0
    var minZ = 0.0

    constructor(middleHeight: Double, middleWidth: Double, middleDepth: Double, minX: Double, minY: Double, minZ: Double) {
        this.middleDepth = middleDepth
        this.middleHeight = middleHeight
        this.middleWidth = middleWidth
        this.minX = minX
        this.minY = minY
        this.minZ = minZ
    }

    /**
     * 指定した側面を描画するかどうか
     */
    fun drawSide(side: Int, draw: Boolean): CustomSelectedBox {
        if (drawSide.size > side) drawSide[side] = draw
        return this
    }

    /**
     * 指定した側面の方向に四角形を伸ばす
     */
    fun setSideLength(side: Int, length: Double): CustomSelectedBox {
        if (sideLength.size > side) sideLength[side] = length
        return this
    }

    /**
     * 指定した値の分だけ広げる
     */
    fun addExtraSpace(space: Double): CustomSelectedBox {
        this.minX -= space
        this.minY -= space
        this.minZ -= space
        this.middleDepth += space
        this.middleHeight += space
        this.middleWidth += space
        for (i in 0..5) {
            sideLength[i] += space
        }
        return this
    }

    fun offsetForDraw(x: Double, y: Double, z: Double): CustomSelectedBox {
        this.minX += x
        this.minY += y
        this.minZ += z
        return this
    }

}