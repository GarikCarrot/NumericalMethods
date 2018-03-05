package hw1.solvers

import hw1.ConditionNumber
import hw1.DoubleMatrix
import hw1.Matrix

class JacobiSolver {
    companion object {
        fun getSolve(a0: Matrix, b: Matrix): Matrix {
            return getSolve(a0, b, 1e-6)
        }

        fun getSolve(a0: Matrix, b: Matrix, e: Double): Matrix {
            val a = a0
            val n = a.size().first
            var result = DoubleMatrix(1, n)
            var nResult = DoubleMatrix(1, n)
            val q = ConditionNumber.getCondition(b)
            while (true) {
                var s = 0.0
                for (i in 0 until n) {
                    for (j in 0 until n) {
                        s += a.get(i, j) * result.get(0, j)
                    }
                }
                for (i in 0 until n) {
                    nResult.set(0, i, (b.get(i, 0) - s) / a.get(i, i))
                }
                val cond = ConditionNumber.getCondition(result - nResult)
                if (cond / (1 - q) < e) break
                result = nResult
                nResult = DoubleMatrix(1, n)
            }
            return nResult
        }
    }
}