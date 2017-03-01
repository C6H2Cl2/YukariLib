package c6h2cl2.YukariLib.Util

/**
 * @author kojin15.
 */
object MathHelperEx {

    var sinTable = DoubleArray(65536)
    val digits: ArrayList<String> = arrayListOf("万", "億", "兆", "京")

    init {
        for (i in 0..65535) {
            sinTable[i] = Math.sin(i / 65536.0 * 2.0 * Math.PI)
        }

        sinTable[0] = 0.0
        sinTable[16384] = 1.0
        sinTable[32768] = 0.0
        sinTable[49152] = 1.0
    }

    fun sin(theta: Double): Double {

        return sinTable[(theta.toFloat() * 10430.378f).toInt() and 65535]
    }

    fun cos(theta: Double): Double {

        return sinTable[(theta.toFloat() * 10430.378f + 16384.0f).toInt() and 65535]
    }

    fun betweenIn(min: Int, x: Int, max: Int): Boolean {
        return min <= x && x <= max
    }

    fun betweenIn(min: Double, x: Double, max: Double): Boolean {
        return min <= x && x <= max
    }

    fun betweenOut(min: Int, x: Int, max: Int): Boolean {
        return min < x && x < max
    }

    fun betweenOut(min: Double, x: Double, max: Double): Boolean {
        return min < x && x < max
    }

    fun cut(min: Int, x: Int, max: Int): Int {
        if (x < min) return min
        if (x > max) return max
        return x
    }

    fun absolute(x: Int): Int {
        return if (x < 0) -x else x
    }

    fun digitInJapanese(x: Int): String = this.digitInJapanese(x.toLong())

    fun digitInJapanese(x: Long): String {
        val valueStr = x.toString()
        val strLength = valueStr.length
        val digits = strLength shr 2
        val rest = strLength - (digits shl 2)

        val ints = IntArray(digits + 1)
        for (i in 0..digits - 1) {
            val s = valueStr.substring(strLength - (4 + (i shl 2)), strLength - (i shl 2))
            ints[i] = Integer.valueOf(s)!!
        }
        if (rest > 0)
            ints[ints.size - 1] = Integer.valueOf(valueStr.substring(0, rest))!!

        val buffer = StringBuffer()
        for (i in ints.size - 1 downTo 1) {
            if (ints[i] != 0) buffer.append(ints[i]).append(this.digits[i - 1])
        }
        if (ints[0] != 0) buffer.append(ints[0])
        return buffer.toString()
    }

}