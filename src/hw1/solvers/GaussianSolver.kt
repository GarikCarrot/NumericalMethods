package hw1.solvers

import hw1.DoubleMatrix
import hw1.Matrix
import hw1.NoSolveException

class GaussianSolver {
    companion object {
        fun getSolve(a0: Matrix, b: Matrix): Matrix {
            if (a0.determinant() == 0.0) throw NoSolveException()
            val a = a0
            val n = a.size().first
            val result = DoubleMatrix(1, n)
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
                    sum -= a.get(step, i) * result.get(0, i)
                }
                sum += b.get(step, 0)
                result.set(0, step, sum / a.get(step, step))
            }
            return result
        }
    }
}