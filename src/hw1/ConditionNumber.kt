package hw1

class ConditionNumber {
    companion object {
        fun getCondition(matrix: DoubleMatrix): Double = getNorm(matrix) * getNorm(matrix.invert())

        fun getCondition(matrix: FractionMatrix): Double = getNorm(matrix) * getNorm(matrix.invert() as FractionMatrix)

        fun getNorm(matrix: DoubleMatrix) : Double {
            var result = 0.0
            for (i in 0 until matrix.size().first) {
                for (j in 0 until matrix.size().second) {
                    var v = matrix.get(i, j)
                    v *= v
                    result += v
                }
            }
            return Math.sqrt(result)
        }

        fun getNorm(matrix: FractionMatrix) : Double {
            var result = Fraction()
            for (i in 0 until matrix.size().first) {
                for (j in 0 until matrix.size().second) {
                    var v = matrix.get(i, j)
                    v *= v
                    result += v
                }
            }
            return Math.sqrt(result.toDouble())
        }
    }
}
