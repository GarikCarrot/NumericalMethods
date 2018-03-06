package hw1

class ConditionNumber {
    companion object {
        fun getCondition(matrix: DoubleMatrix): Double = getNorm(matrix) * getNorm(matrix.invert())

        fun getCondition(matrix: FractionMatrix): Double = getNorm(matrix) * getNorm(matrix.invert())

        fun getNorm(matrix: DoubleMatrix) : Double {
            var result = 0.0
            for (i in 0 until matrix.size().first) {
                var sum = 0.0
                for (j in 0 until matrix.size().second) {
                    var v = matrix.get(i, j)
                    v = Math.abs(v)
                    sum += v
                }
                result = Math.max(sum, result)
            }
            return Math.sqrt(result)
        }

        fun getNorm(matrix: FractionMatrix) : Double {
            var result = 0.0
            for (i in 0 until matrix.size().first) {
                var sum = Fraction()
                for (j in 0 until matrix.size().second) {
                    sum += matrix.get(i, j).abs()
                }
                result = Math.max(sum.toDouble(), result)
            }
            return result
        }
    }
}
