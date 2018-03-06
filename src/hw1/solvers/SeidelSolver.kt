package hw1.solvers

import hw1.*

class SeidelSolver {
    companion object {
        fun getSolve(a0: DoubleMatrix, b: DoubleMatrix, x0: DoubleMatrix): DoubleMatrix = getSolve(a0, b, x0, 1e-6)

        fun getSolve(a0: DoubleMatrix, b: DoubleMatrix, x0: DoubleMatrix, eps: Double): DoubleMatrix {
            val n = a0.size().first
            val l = a0.clone()
            val e = DoubleMatrix(n, n)
            for (i in 0 until n) {
                for (j in i + 1 until n) {
                    l.set(i, j, 0.0)
                }
                e.set(i, i, 1.0)
            }
            val r = a0 - l
            val q = ConditionNumber.getNorm((e - l).invert() * r)
            var result = x0
            var nResult = DoubleMatrix(n, 1)
            if (q > 1) throw NoSolveException()
            while (true) {
                for (i in 0 until n) {
                    var s = 0.0
                    for (j in 0 until i) {
                        s += a0.get(i, j) * nResult.get(j, 0)
                    }
                    for (j in i + 1 until n) {
                        s += a0.get(i, j) * result.get(j, 0)
                    }
                    nResult.set(i, 0, (b.get(i, 0) - s) / a0.get(i, i))
                }
                val cond = ConditionNumber.getNorm(result - nResult)
                if (cond < eps) break
                result = nResult
                nResult = DoubleMatrix(n, 1)
            }
            return nResult
        }

        fun getSolve(a0: FractionMatrix, b: FractionMatrix, x0: FractionMatrix, eps: Double): FractionMatrix {
            val n = a0.size().first
            val l = a0.clone()
            val e = FractionMatrix(n, n)
            for (i in 0 until n) {
                for (j in i + 1 until n) {
                    l.set(i, j, Fraction())
                }
                e.set(i, i, Fraction())
            }
            val r = a0 - l
            val q = ConditionNumber.getNorm((e - l).invert() * r)
            var result = x0
            var nResult = FractionMatrix(n, 1)
            if (q > 1) throw NoSolveException()
            while (true) {
                for (i in 0 until n) {
                    var s = Fraction()
                    for (j in 0 until i) {
                        s += a0.get(i, j) * nResult.get(j, 0)
                    }
                    for (j in i + 1 until n) {
                        s += a0.get(i, j) * result.get(j, 0)
                    }
                    nResult.set(i, 0, (b.get(i, 0) - s) / a0.get(i, i))
                }
                val cond = ConditionNumber.getNorm(result - nResult)
                if (cond < eps) break
                result = nResult
                nResult = FractionMatrix(n, 1)
            }
            return nResult
        }

    }

}