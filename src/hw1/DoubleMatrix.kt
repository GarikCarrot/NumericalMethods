package hw1

class DoubleMatrix(private var n: Int, private var m: Int) : Matrix {


    private var values = Array(n) { DoubleArray(m) }

    override fun plus(matrix: Matrix): Matrix = assign { it += matrix }

    override fun minus(matrix: Matrix): Matrix = assign { it -= matrix}

    override fun times(matrix: Matrix) = assign { it *= matrix }

    private fun assign(v: (DoubleMatrix) -> Unit) : DoubleMatrix {
        val result = DoubleMatrix(n, m)
        result += this
        v(result)
        return result
    }

    override fun plusAssign(matrix: Matrix) {
        for (i in 0 until n) {
            for (j in 0 until m) {
                values[i][j] += matrix.get(i, j)
            }
        }
    }

    override fun minusAssign(matrix: Matrix) {
        for (i in 0 until n) {
            for (j in 0 until m) {
                values[i][j] -= matrix.get(i, j)
            }
        }
    }

    override fun timesAssign(matrix: Matrix) {
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

    override fun trans(): Matrix {
        val nValues = Array(m) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0 until m) {
                nValues[j][i] = values[i][j]
            }
        }
        values = nValues
        n = m.also { m = n } //swap
        return this
    }

    override fun invert(): Matrix {
        val result = trans() as DoubleMatrix
        val d = determinant()
        for (i in 0 until n) {
            for (j in 0 until m) {
                result.values[i][j] *= d
            }
        }
        return result
    }

    override fun set(i: Int, j: Int, value: Double) {
        values[i][j] = value
    }

    override fun get(i: Int, j: Int): Double = values[i][j]

    override fun change(i: Int, j: Int, change: (Double) -> Double) {
        set(i, j, change(get(i, j)))
    }

    override fun determinant(): Double {
        if (n != m) throw MatrixSizeException()
        var result = 0.0
        for (i in 0 until n) {
            var a = 1.0
            var b = 1.0
            for (j in 0 until n) {
                a *= get(i, (n + j + i) % n)
                b *= get(i, (n + j - i) % n)
            }
            result += a - b
        }
        return result
    }

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

}