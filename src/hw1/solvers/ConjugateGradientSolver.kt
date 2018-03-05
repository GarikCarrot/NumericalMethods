package hw1.solvers

import hw1.ConditionNumber
import hw1.DoubleMatrix

class ConjugateGradientSolver {
    companion object {
        fun getSolve(a0: DoubleMatrix, b0: DoubleMatrix, x0: DoubleMatrix, epsilon: Double): DoubleMatrix {

            var xkm = x0
            var rkm = b0 - a0 * x0
            var zkm = rkm

            var k = 0
            while (true) {
                k++
                val ak = rkm.scalarTimes(rkm) / (a0 * zkm).scalarTimes(zkm)
                val xk = xkm + zkm.multiply(ak)
                val rk = rkm - a0 * zkm.multiply(ak)
                val bk = rk.scalarTimes(rk) / rkm.scalarTimes(rkm)
                val zk = rk + zkm.multiply(bk)

                xkm = xk
                rkm = rk
                zkm = zk

                if (ConditionNumber.getNorm(rk) / ConditionNumber.getNorm(b0) < epsilon)
                    break
            }

            return rkm

        }
    }
}