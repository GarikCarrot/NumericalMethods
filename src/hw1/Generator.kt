package hw1

import java.lang.Math.abs


class Generator {
    companion object {
        fun rand() = (Math.random() * 100).toInt()

        fun generateSymmetric(size: Int): DoubleMatrix {
            while (true) {
                val matrix = generateRandom(size, size)
                (0 until matrix.n).forEach { i ->
                    (i + 1 until matrix.n).forEach { j ->
                        matrix[i, j] = matrix[j, i]
                    }
                }
                assert(matrix == matrix.trans())
                if (matrix.determinant() > 0) return matrix
            }
        }

        fun generateDiagonallyDominant(size: Int): DoubleMatrix {
            val matrix = generateRandom(size, size)
            (0 until matrix.n).forEach { i ->
                val j = (0 until matrix.m).sumByDouble { j -> matrix[i, j] }
                matrix[i, i] = j + rand()
            }
            return matrix
        }

        fun generateBad(size: Int): DoubleMatrix {
            val result = DoubleMatrix(size, size)
            for (i in 0 until size) {
                for (j in 0 until size) {
                    result.set(i, j, (1.0 / (i + j + 1)))
                }
            }
            return result
        }

        fun generateRandom(n: Int, m: Int): DoubleMatrix {
            val result = DoubleMatrix(n, m)
            for (i in 0 until n) {
                for (j in 0 until m) {
                    val a = rand()
                    val b = rand()
                    result.set(i, j, "$a.$b".toDouble())
                }
            }
            return result
        }

        fun generateBadFraction(size: Int): FractionMatrix {
            val result = FractionMatrix(size, size)
            for (i in 0 until size) {
                for (j in 0 until size) {
                    result.set(i, j, Fraction("1", (i + j + 1).toString()))
                }
            }
            return result
        }


        fun toFraction(v: DoubleMatrix) : FractionMatrix {
            val result = FractionMatrix(v.size().first, v.size().second)
            (0 until v.size().first).forEach { i ->
                (0 until v.size().second).forEach { j ->
                    result.set(i, j, Fraction(v[i, j]))
                }
            }
            return result
        }
    }
}