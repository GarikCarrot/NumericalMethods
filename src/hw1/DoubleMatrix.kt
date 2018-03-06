package hw1

import java.util.*

open class DoubleMatrix(n: Int, m: Int, values: Array<Array<Double>>) : Matrix<DoubleMatrix, Double>(n, m, values), Getable<Double> {
    override fun scalarTimes(matrix: DoubleMatrix): Double {
        return values
                .zip(matrix.values)
                .foldRight(0.0) { x, acc ->
                    x.first.zip(x.second)
                            .foldRight(0.0) { y, acc2 ->
                                y.first * y.second + acc2
                            } + acc
                }
    }

    override fun construct(n: Int, m: Int, values: List<Array<Double>>): DoubleMatrix = DoubleMatrix(n, m, values.toTypedArray())

    constructor(n: Int, m: Int) : this(n, m, Array(n, { Array(m, { _ -> 0.0 }) }))

    override fun plus(matrix: DoubleMatrix): DoubleMatrix = apply(matrix) { it.first + it.second }

    override fun minus(matrix: DoubleMatrix): DoubleMatrix = apply(matrix) { it.first - it.second }

    override fun times(matrix: DoubleMatrix): DoubleMatrix {
        if (m != matrix.n) throw MatrixSizeException()

        val newM = matrix.m

        val newValues = Array(n) { Array(newM) { 0.0 } }
        (0 until n).forEach { i ->
            (0 until newM).forEach { j ->
                (0 until m).forEach { k ->
                    newValues[i][j] += values[i][k] * matrix.values[k][j]
                }
            }
        }
        return DoubleMatrix(n, newM, newValues)
    }

    override fun apply(matrix: DoubleMatrix, function: (Pair<Double, Double>) -> Double): DoubleMatrix {
        if (n != matrix.n || m != matrix.m) throw MatrixSizeException()

        return DoubleMatrix(n, m, values.zip(matrix.values).map {
            it.first.zip(it.second).map {
                function(it)
            }.toTypedArray()
        }.toTypedArray())
    }

    override fun trans(): DoubleMatrix {
        val newValues = Array(m) { Array(n) { 0.0 } }

        (0 until n).forEach { i ->
            (0 until m).forEach { j -> newValues[j][i] = values[i][j] }
        }
        return DoubleMatrix(m, n, newValues)
    }

    override fun invert(): DoubleMatrix {
        if (n != m) throw MatrixSizeException()
        val newValues = Array(n) { Array(m) { 0.0 } }

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

        return DoubleMatrix(n, m, newValues).trans()
    }

    override operator fun set(i: Int, j: Int, value: Double) {
        values[i][j] = value
    }

    override operator fun get(i: Int, j: Int): Double = values[i][j]

    override fun change(i: Int, j: Int, change: (Double) -> Double) {
        set(i, j, change(get(i, j)))
    }

    fun multiply(by: Double): DoubleMatrix {
        val clone = clone()
        (0 until n).forEach { i ->
            (0 until m).forEach {j ->
                clone.change(i, j) { it * by}
            }
        }
        return clone
    }

    override fun determinant(): Double = subMatrix().determinant()

    override fun equals(other: Any?): Boolean {
        if (other !is DoubleMatrix) return false
        if (size() != other.size()) return false
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (get(i, j) != other.get(i, j))
                    return false
            }
        }
        return true
    }

    private fun subMatrix(): SubMatrix = SubMatrix(this, n, n, n)

    private fun subMatrix(wx: Int, wy: Int): SubMatrix = SubMatrix(this, wx, wy, n - 1)

    override fun toString(): String {
        return  values.fold("[${n}x${m}]") {acc, doubles -> "$acc\n${Arrays.deepToString(doubles)}"}
    }

    private class SubMatrix(private val parent: Getable<Double>, private val wx: Int, private val wy: Int, private val size: Int) : Getable<Double> {
        override fun get(i: Int, j: Int): Double {
            val x = if (i < wx) i else i + 1
            val y = if (j < wy) j else j + 1
            return parent.get(x, y)
        }

        override fun determinant(): Double {
            if (size == 1) return get(0, 0)
            var result = 0.0
            for (i in 0 until size) {
                val d = SubMatrix(this, i, 0, size - 1).determinant()
                if (i % 2 == 0) result += get(i, 0) * d
                else result -= get(i, 0) * d
            }
            return result
        }
    }
}