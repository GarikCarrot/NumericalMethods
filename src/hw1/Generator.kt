package hw1

import java.lang.Math.abs


class Generator {
    companion object {
        fun rand() = (Math.random() * 100).toInt()
        fun generateGood(size: Int) : DoubleMatrix {
            val matrix = generateRandom(size, size)
            (0 until matrix.n).forEach {i  ->
                val j = (0 until matrix.m).maxBy { j -> matrix[i, j] }!!
                matrix[i, i] = matrix[i, j] + abs(rand())
            }
        }

        fun generateBad() : DoubleMatrix {
            TODO()
        }

        fun generateRandom(size: Int) : DoubleMatrix {
            val result = DoubleMatrix(size, size)
            for (i in 0 until size) {
                for (j in 0 until size) {
                    val a = rand()
                    val b = rand()
                    result.set(i, j, "$a.$b".toDouble())
                }
            }
            return result
        }
    }
}