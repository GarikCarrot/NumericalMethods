//package hw1
//
//import java.math.BigDecimal
//
//class BigDecimalMatrix(n: Int, m: Int) : AbstractMatrix(n, m) {
//    private var values = Array(n) { Array(m) { BigDecimal(1) } }
//
//    override fun trans(): Matrix {
//        val nValues = Array(m) { Array(n) { BigDecimal(1) } }
//        IntRange(0, n).forEach { i ->
//            IntRange(0, m).forEach { j ->
//                nValues[j][i] = values[i][j]
//            }
//        }
//        values = nValues
//        n = m.also { m = n } //swap
//        return this
//    }
//
//    override fun set(i: Int, j: Int, value: Double) {
//        values[i][j] = BigDecimal(value)
//    }
//
//    override fun get(i: Int, j: Int): Double = values[i][j].toDouble()
//
//    override fun plus(i: Int, j: Int, value: Double) {
//        values[i][j] += BigDecimal(value)
//    }
//
//    override fun minus(i: Int, j: Int, value: Double) {
//        values[i][j] -= BigDecimal(value)
//    }
//
//    override fun times(i: Int, j: Int, value: Double) {
//        values[i][j] *= BigDecimal(value)
//    }
//
//    override fun div(i: Int, j: Int, value: Double) {
//        values[i][j] /= BigDecimal(value)
//    }
//
//    override fun times(matrix: Matrix): Matrix {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//}