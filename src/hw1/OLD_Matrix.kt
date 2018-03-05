package hw1


interface Matrix<T>{
    operator fun plus(matrix: T): T
    operator fun minus(matrix: T): T
    operator fun times(matrix: T): T

    fun trans(): T
    fun invert(): T

    fun set(i: Int, j: Int, value: Double)
    fun get(i: Int, j: Int): Double
    fun change(i: Int, j: Int, change: (Double) -> Double)
    fun determinant(): Double

    fun size(): Pair<Int, Int>
}


interface OLD_Matrix {
    operator fun plus(matrix: OLD_Matrix): OLD_Matrix
    operator fun minus(matrix: OLD_Matrix): OLD_Matrix
    operator fun times(matrix: OLD_Matrix): OLD_Matrix
    operator fun plusAssign(matrix: OLD_Matrix)
    operator fun minusAssign(matrix: OLD_Matrix)
    operator fun timesAssign(matrix: OLD_Matrix)

    fun trans(): OLD_Matrix
    fun invert(): OLD_Matrix

    fun set(i: Int, j: Int, value: Double)
    fun get(i: Int, j: Int): Double
    fun change(i: Int, j: Int, change: (Double) -> Double)
    fun determinant(): Double

    fun size(): Pair<Int, Int>
}