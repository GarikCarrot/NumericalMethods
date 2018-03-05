package hw1


abstract class Matrix<T, V> (val n: Int, val m: Int, protected val values: Array<Array<V>>) {

    abstract operator fun plus(matrix: T): T
    abstract operator fun minus(matrix: T): T
    abstract operator fun times(matrix: T): T

    abstract fun trans(): T
    abstract fun invert(): T

    abstract fun set(i: Int, j: Int, value: V)
    abstract fun get(i: Int, j: Int): V
    abstract fun change(i: Int, j: Int, change: (V) -> V)
    abstract fun determinant(): V

    fun size(): Pair<Int, Int> = n to m

    abstract fun apply(matrix: T, function: (Pair<V, V>) -> V): T

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