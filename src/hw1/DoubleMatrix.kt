package hw1

class DoubleMatrix(n: Int, m: Int) : AbstractMatrix(n, m) {
    private var values = Array(n) { DoubleArray(m) }

    override fun trans(): Matrix {
        val nValues = Array(m) { DoubleArray(n) }
        IntRange(0, n).forEach { i ->
            IntRange(0, m).forEach { j ->
                nValues[j][i] = values[i][j]
            }
        }
        values = nValues
        n = m.also { m = n } //swap
        return this
    }

    override fun set(i: Int, j: Int, value: Double) {
        values[i][j] = value
    }

    override fun get(i: Int, j: Int): Double = values[i][j]

    override fun plus(i: Int, j: Int, value: Double) {
        values[i][j] += value
    }

    override fun minus(i: Int, j: Int, value: Double) {
        values[i][j] -= value
    }

    override fun times(i: Int, j: Int, value: Double) {
        values[i][j] *= value
    }

    override fun div(i: Int, j: Int, value: Double) {
        values[i][j] /= value
    }

    override fun size(): Pair<Int, Int> = Pair(n, m)

}