package hw1

open class DoubleMatrix(n: Int, m: Int, values: Array<Array<Double>>) : Matrix<DoubleMatrix, Double>(n, m, values), Getable<Double> {

    constructor(n: Int, m: Int) : this(n, m, Array(n, { Array(m, { _ -> 0.0 }) }))
    
    override fun plus(matrix: DoubleMatrix): DoubleMatrix = apply(matrix) { it.first + it.second }

    override fun minus(matrix: DoubleMatrix): DoubleMatrix = apply(matrix) { it.first - it.second }

    override fun times(matrix: DoubleMatrix): DoubleMatrix {
        if (m != matrix.n || n != matrix.m) throw MatrixSizeException()

        val newM = matrix.m

        val newValues = Array(n) { Array(newM) { 0.0 } }
        (0 until n).forEach { i ->
            (0 until m).forEach { j ->
                (0 until newM).forEach { k ->
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
        val newValues = Array(n) { Array(m) { 0.0 } }

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

    fun clone(): DoubleMatrix = DoubleMatrix(n, m, values.map { it.copyOf() }.toTypedArray())

    override fun set(i: Int, j: Int, value: Double) {
        values[i][j] = value
    }

    override fun get(i: Int, j: Int): Double = values[i][j]

    override fun change(i: Int, j: Int, change: (Double) -> Double) {
        set(i, j, change(get(i, j)))
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

    private class SubMatrix {
        private val parentM: DoubleMatrix?
        private val parent: SubMatrix?
        private val wx: Int
        private val wy: Int
        private val size: Int

        constructor(parent: DoubleMatrix, wx: Int, wy: Int, size: Int) {
            this.parentM = parent
            this.parent = null
            this.wx = wx
            this.wy = wy
            this.size = size
        }

        constructor(parent: SubMatrix, wx: Int, wy: Int, size: Int) {
            this.parentM = null
            this.parent = parent
            this.wx = wx
            this.wy = wy
            this.size = size
        }

        private fun get(i: Int, j: Int): Double {
            val x = if (i < wx) i else i + 1
            val y = if (j < wy) j else j + 1
            return parentM?.get(x, y) ?: parent!!.get(x, y)
        }

        fun determinant(): Double {
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