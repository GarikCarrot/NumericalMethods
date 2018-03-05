package hw1.solvers

import hw1.ConditionNumber
import hw1.DoubleMatrix
import hw1.Matrix
import hw1.NoSolveException

class JacobiSolver {
    companion object {
        fun getSolve(a0: Matrix, b: Matrix, x0: Matrix): Matrix = getSolve(a0, b, x0, 1e-6)

        fun getSolve(a0: Matrix, b: Matrix, x0: Matrix, e: Double): Matrix {
            val a = a0
            val n = a.size().first
            var result = x0
            var nResult = DoubleMatrix(1, n)
            val q = ConditionNumber.getNorm(b)
            if (q > 1) throw NoSolveException()
            while (true) {
                for (i in 0 until n) {
                    var s = 0.0
                    for (j in 0 until n) {
                        if (i != j)
                            s += a.get(i, j) * result.get(0, j)
                    }
                    nResult.set(0, i, (b.get(i, 0) - s) / a.get(i, i))
                }
                val cond = ConditionNumber.getNorm(result - nResult)
                if (cond / (1 - q) < e) break
                result = nResult
                nResult = DoubleMatrix(1, n)
            }
            return nResult
        }
    }
}