package hw1.solvers

import hw1.ConditionNumber
import hw1.DoubleMatrix
import hw1.NoSolveException

class JacobiSolver {
    companion object {
        fun getSolve(a0: DoubleMatrix, b: DoubleMatrix, x0: DoubleMatrix): DoubleMatrix = getSolve(a0, b, x0, 1e-6)

        fun getSolve(a0: DoubleMatrix, b: DoubleMatrix, x0: DoubleMatrix, e: Double): DoubleMatrix {
            val n = a0.size().first
            val r = a0.clone()
            for (i in 0 until n) {
                r.set(i, i, 0.0)
            }
            val d = a0 - r
            val q = ConditionNumber.getNorm(d.invert() * r)
            var result = x0
            var nResult = DoubleMatrix(n, 1)
            if (q > 1) throw NoSolveException()
            while (true) {
                for (i in 0 until n) {
                    var s = 0.0
                    for (j in 0 until n) {
                        if (i != j)
                            s += a0.get(i, j) * result.get(j, 0)
                    }
                    nResult.set(i, 0, (b.get(i, 0) - s) / a0.get(i, i))
                }
                val cond = ConditionNumber.getNorm(result - nResult)
                if (cond / (1 - q) < e) break
                result = nResult
                nResult = DoubleMatrix(n, 1)
            }
            return nResult
        }
    }
}