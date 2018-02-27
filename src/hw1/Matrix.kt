package hw1

interface Matrix {
    operator fun plus(matrix: Matrix): Matrix
    operator fun minus(matrix: Matrix): Matrix
    operator fun times(matrix: Matrix): Matrix
    fun trans(): Matrix

    fun set(i: Int, j: Int, value: Double)
    fun get(i: Int, j: Int): Double

    fun plus(i: Int, j: Int, value: Double)
    fun minus(i: Int, j: Int, value: Double)
    fun times(i: Int, j: Int, value: Double)
    fun div(i: Int, j: Int, value: Double)

    fun size(): Pair<Int, Int>
}