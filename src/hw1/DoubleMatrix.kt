package hw1

open class DoubleMatrix(private var n: Int, private var m: Int) : OLD_Matrix {


    private var values = Array(n) { DoubleArray(m) }

    override fun plus(matrix: OLD_Matrix): OLD_Matrix = assign { it += matrix }

    override fun minus(matrix: OLD_Matrix): OLD_Matrix = assign { it -= matrix }

    override fun times(matrix: OLD_Matrix) = assign { it *= matrix }

    private fun assign(v: (DoubleMatrix) -> Unit): DoubleMatrix {
        val result = DoubleMatrix(n, m)
        result += this
        v(result)
        return result
    }

    override fun plusAssign(matrix: OLD_Matrix) {
        for (i in 0 until n) {
            for (j in 0 until m) {
                values[i][j] += matrix.get(i, j)
            }
        }
    }

    override fun minusAssign(matrix: OLD_Matrix) {
        for (i in 0 until n) {
            for (j in 0 until m) {
                values[i][j] -= matrix.get(i, j)
            }
        }
    }

    override fun timesAssign(matrix: OLD_Matrix) {
        val nM = matrix.size().second
        if (size().second != matrix.size().first) throw MatrixSizeException()
        val nValues = Array(n) { DoubleArray(nM) }
        for (i in 0 until n) {
            for (j in 0 until m) {
                for (k in 0 until nM) {
                    nValues[i][j] += get(i, k) * matrix.get(k, j)
                }
            }
        }
        values = nValues
    }

    override fun trans(): OLD_Matrix {
        val result = DoubleMatrix(m, n)
        for (i in 0 until n) {
            for (j in 0 until m) {
                result.values[j][i] = values[i][j]
            }
        }
        return result
    }

    override fun invert(): OLD_Matrix {
        if (n != m) throw MatrixSizeException()
        val result = DoubleMatrix(n, m)
        val d = determinant()
        for (i in 0 until n) {
            for (j in 0 until m) {
                val value = subMatrix(i, j).determinant() / d
                if ((i + j) % 2 == 0) {
                    result.values[i][j] = value
                } else {
                    result.values[i][j] = -value
                }
            }
        }
        return result.trans()
    }

    override fun set(i: Int, j: Int, value: Double) {
        values[i][j] = value
    }

    override fun get(i: Int, j: Int): Double = values[i][j]

    override fun change(i: Int, j: Int, change: (Double) -> Double) {
        set(i, j, change(get(i, j)))
    }

    override fun determinant(): Double = subMatrix().determinant()

    override fun size(): Pair<Int, Int> = Pair(n, m)

    override fun equals(other: Any?): Boolean {
        if (other !is DoubleMatrix) return false
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