package hw1

class Generator {
    companion object {
        fun generateGood() : DoubleMatrix {
            TODO()
        }

        fun generateBad() : DoubleMatrix {
            TODO()
        }

        fun generateRandom(size: Int) : DoubleMatrix {
            val result = DoubleMatrix(size, size)
            for (i in 0 until size) {
                for (j in 0 until size) {
                    val a = (Math.random() * 100).toInt()
                    val b = (Math.random() * 100).toInt()
                    result.set(i, j, "$a.$b".toDouble())
                }
            }
            return result
        }
    }
}