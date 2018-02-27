package hw1

abstract class AbstractMatrix(protected var n: Int, protected var m: Int) : Matrix {
    override fun plus(matrix: Matrix): Matrix {
        if (matrix.size() != size()) throw MatrixSizeException()
        IntRange(0, n).forEach { i ->
            IntRange(0, m).forEach { j ->
                plus(i, j, matrix.get(i, j))
            }
        }
        return this
    }

    override fun minus(matrix: Matrix): Matrix {
        if (matrix.size() != size()) throw MatrixSizeException()
        IntRange(0, n).forEach { i ->
            IntRange(0, m).forEach { j ->
                minus(i, j, matrix.get(i, j))
            }
        }
        return this
    }

    override fun times(matrix: Matrix): Matrix {
        if (matrix.size() != size()) throw MatrixSizeException()
        IntRange(0, n).forEach { i ->
            IntRange(0, m).forEach { j ->
                times(i, j, matrix.get(i, j))
            }
        }
        return this
    }

    override fun size(): Pair<Int, Int> = Pair(n, m)

}