package hw1

interface Matrix {
    operator fun plus(matrix: Matrix): Matrix
    operator fun minus(matrix: Matrix): Matrix
    operator fun times(matrix: Matrix): Matrix
    operator fun plusAssign(matrix: Matrix)
    operator fun minusAssign(matrix: Matrix)
    operator fun timesAssign(matrix: Matrix)

    fun trans(): Matrix
    fun invert(): Matrix

    fun set(i: Int, j: Int, value: Double)
    fun get(i: Int, j: Int): Double
    fun change(i: Int, j: Int, change: (Double) -> Double)
    fun determinant(): Double

    fun size(): Pair<Int, Int>
}