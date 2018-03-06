package hw1.solvers

import hw1.DoubleMatrix
import hw1.NoSolveException

class GaussianSolver {
    companion object {
        fun getSolve(a0: DoubleMatrix, b0: DoubleMatrix): DoubleMatrix {
            if (a0.determinant() == 0.0) throw NoSolveException()
            val a = a0.clone()
            val b = b0.clone()
            val n = a.size().first
            val result = DoubleMatrix(n, 1)
            for (step in 0 until n - 1) {
                val t = a.get(step, step)
                for (i in step + 1 until n) {
                    val tt = a.get(i, step) / t
                    for (j in 0 until n) {
                        a.change(i, j, { it - tt * a.get(step, j) })
                    }
                    b.change(i, 0, { it - tt * b.get(step, 0) })
                }
            }
            for (step in n - 1 downTo 0) {
                var sum = 0.0
                for (i in step + 1 until n) {
                    sum -= a.get(step, i) * result.get(i, 0)
                }
                sum += b.get(step, 0)
                result.set(step, 0, sum / a.get(step, step))
            }
            return result
        }
    }
}