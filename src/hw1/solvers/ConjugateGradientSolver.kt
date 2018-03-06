package hw1.solvers

import hw1.ConditionNumber
import hw1.DoubleMatrix

class ConjugateGradientSolver {
    companion object {
        fun getSolve(a0: DoubleMatrix, b0: DoubleMatrix, x0: DoubleMatrix, epsilon: Double): DoubleMatrix {

            var xkm = x0
            var rkm = b0 - a0 * x0.trans()
            var pkm = rkm

            var k = 0
            while (true) {
                k++
                val ak = rkm.scalarTimes(rkm) / (a0 * pkm).scalarTimes(pkm)
                val xk = (xkm.trans() + pkm.multiply(ak)).trans()
                val rk = rkm - a0 * pkm.multiply(ak)
                val bk = rk.scalarTimes(rk) / rkm.scalarTimes(rkm)
                val pk = rk + pkm.multiply(bk)

                xkm = xk
                rkm = rk
                pkm = pk

                val d = ConditionNumber.getNorm(rk) / ConditionNumber.getNorm(b0)
                println("$d")
                Thread.sleep(500)
                if (d < epsilon)
                    break
            }

            return rkm
        }
    }
}