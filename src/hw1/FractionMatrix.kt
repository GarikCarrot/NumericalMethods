package hw1

import java.util.*

interface Getable<out V> {
    fun determinant(): V
    fun get(i: Int, j: Int): V
}

class FractionMatrix : Matrix<FractionMatrix, Fraction>, Getable<Fraction> {

    private val n: Int
    private val m: Int

    private val values: Array<Array<Fraction>>

    constructor(n: Int, m: Int, values: Array<Array<Fraction>>) {
        this.n = n
        this.m = m
        this.values = values
    }

    constructor(n: Int, m: Int) : this(n, m, Array(n, { Array(m, { _ -> Fraction() }) }));


    private fun apply(matrix: FractionMatrix, function: (Pair<Fraction, Fraction>) -> Fraction): FractionMatrix {
        if (n != matrix.n || m != matrix.m) throw MatrixSizeException()

        return FractionMatrix(n, m, values.zip(matrix.values).map {
            it.first.zip(it.second).map {
                function(it)
            }.toTypedArray()
        }.toTypedArray())
    }

    override fun plus(matrix: FractionMatrix): FractionMatrix = apply(matrix) { it.first + it.second }

    override fun minus(matrix: FractionMatrix): FractionMatrix = apply(matrix) { it.first - it.second }

    override fun times(matrix: FractionMatrix): FractionMatrix {
        if (m != matrix.n || n != matrix.m) throw MatrixSizeException()

        val newM = matrix.m

        val newValues: Array<Array<Fraction>> = Array(n) { Array(newM) { Fraction() } }
        (0 until n).forEach { i ->
            (0 until m).forEach { j ->
                (0 until newM).forEach { k ->
                    newValues[i][j] += values[i][k] * matrix.values[k][j]
                }
            }
        }
        return FractionMatrix(n, newM, newValues)
    }

    override fun trans(): FractionMatrix {
        val newValues: Array<Array<Fraction>> = Array(n) { Array(m) { Fraction() } }

        (0 until n).forEach { i ->
            (0 until m).forEach { j -> newValues[j][i] = values[i][j] }
        }
        return FractionMatrix(m, n, newValues)
    }

    override fun invert(): FractionMatrix {
        if (n != m) throw MatrixSizeException()
        val newValues: Array<Array<Fraction>> = Array(n) { Array(m) { Fraction() } }

        val d = determinant()
        (0 until n).forEach { i ->
            (0 until m).forEach { j ->
                val value = subMatrix(i, j).determinant() / d
                if ((i + j) % 2 == 0) {
                    newValues[i][j] = value
                } else {
                    newValues[i][j] = -value
                }
            }
        }

        return FractionMatrix(n, m, newValues).trans()
    }

    fun clone(): FractionMatrix = FractionMatrix(n, m, values.map { it.copyOf() }.toTypedArray())

    override operator fun set(i: Int, j: Int, value: Fraction) {
        values[i][j] = value
    }

    override operator fun get(i: Int, j: Int): Fraction = Fraction(values[i][j])

    override fun change(i: Int, j: Int, change: (Fraction) -> Fraction) = set(i, j, change(get(i, j)))

    override fun determinant(): Fraction = subMatrix().determinant()

    override fun size(): Pair<Int, Int> = Pair(n, m)

    override fun equals(other: Any?): Boolean {
        if (other !is FractionMatrix) return false
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (this[i, j] != other[i, j])
                    return false
            }
        }
        return true
    }


    private fun subMatrix(): SubMatrix = SubMatrix(this, n, n, n)

    private fun subMatrix(wx: Int, wy: Int): SubMatrix = SubMatrix(this, wx, wy, n - 1)
    override fun hashCode(): Int {
        var result = n
        result = 31 * result + m
        result = 31 * result + Arrays.hashCode(values)
        return result
    }


    private class SubMatrix(private val parent: Getable<Fraction>, private val wx: Int, private val wy: Int, private val size: Int) : Getable<Fraction> {

        override fun get(i: Int, j: Int): Fraction {
            val x = if (i < wx) i else i + 1
            val y = if (j < wy) j else j + 1
            return parent.get(x, y)
        }

        override fun determinant(): Fraction {
            if (size == 1) return get(0, 0)
            var result = Fraction()
            for (i in 0 until size) {
                val d = SubMatrix(this, i, 0, size - 1).determinant()
                result +=
                        if (i % 2 == 0) get(i, 0) * d
                        else -get(i, 0) * d
            }
            return result
        }
    }

}