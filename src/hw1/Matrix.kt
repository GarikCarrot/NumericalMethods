package hw1


abstract class Matrix<T : Matrix<T, V>, V>(val n: Int, val m: Int, protected val values: Array<Array<V>>) {

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

    fun clone(): T = construct(n, m, values.map { it.copyOf() })

    abstract fun construct(n: Int, m: Int, values: List<Array<V>>): T // ЖЭ - женерики
}


interface Getable<out V> {
    fun determinant(): V
    fun get(i: Int, j: Int): V
}
