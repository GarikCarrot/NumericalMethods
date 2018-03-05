package hw1

class ConditionNumber {
    companion object {
        fun getCondition(matrix: Matrix): Double {
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

        fun getCondition(matrix: FractionMatrix): Double {
            var result = FractionMatrix.Fraction()
            for (i in 0 until matrix.size().first) {
                for (j in 0 until matrix.size().second) {
                    var v = matrix.getFraction(i, j)
                    v *= v
                    result += v
                }
            }
            return Math.sqrt(result.toDouble())
        }
    }
}
