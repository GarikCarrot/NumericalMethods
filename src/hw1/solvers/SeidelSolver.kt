package hw1.solvers

import hw1.ConditionNumber
import hw1.DoubleMatrix
import hw1.NoSolveException

class SeidelSolver {
    companion object {
        fun getSolve(a0: DoubleMatrix, b: DoubleMatrix, x0: DoubleMatrix): DoubleMatrix = getSolve(a0, b, x0, 1e-6)

        fun getSolve(a0: DoubleMatrix, b: DoubleMatrix, x0: DoubleMatrix, eps: Double): DoubleMatrix {
            val n = a0.size().first
            val l = a0.clone()
            val e = DoubleMatrix(n, n)
            for (i in 0 until n) {
                for (j in i until n) {
                    l.set(i, j, 0.0)
                }
                e.set(i, i, 1.0)
            }
            val r = a0 - l
            val q = ConditionNumber.getNorm((e - l).invert() * r)
            var result = x0
            var nResult = DoubleMatrix(1, n)
            if (q > 1) throw NoSolveException()
            while (true) {
                for (i in 0 until n) {
                    var s = 0.0
                    for (j in 0 until i) {
                        s += a0.get(i, j) * nResult.get(0, j)
                    }
                    for (j in i + 1 until n) {
                        s += a0.get(i, j) * result.get(0, j)
                    }
                    nResult.set(0, i, (b.get(i, 0) - s) / a0.get(i, i))
                }
                val cond = ConditionNumber.getNorm(result - nResult)
                if (cond < eps) break
                result = nResult
                nResult = DoubleMatrix(1, n)
            }
            return nResult
        }
    }

}