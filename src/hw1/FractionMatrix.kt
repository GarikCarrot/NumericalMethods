package hw1

import java.math.BigInteger

class FractionMatrix(n: Int, m: Int) : AbstractMatrix(n, m) {
    private var values = Array(n) { Array(m) { Fraction() } }

    override fun trans(): Matrix {
        val nValues = Array(m) { Array(n) { Fraction() } }
        IntRange(0, n).forEach { i ->
            IntRange(0, m).forEach { j ->
                nValues[j][i] = values[i][j]
            }
        }
        values = nValues
        n = m.also { m = n } //swap
        return this
    }

    override fun set(i: Int, j: Int, value: Double) {
        values[i][j].valueOf(value)
    }

    override fun get(i: Int, j: Int): Double = values[i][j].toDouble()

    override fun plus(i: Int, j: Int, value: Double) {
        values[i][j] += Fraction(value)
    }

    override fun minus(i: Int, j: Int, value: Double) {
        values[i][j] -= Fraction(value)
    }

    override fun times(i: Int, j: Int, value: Double) {
        values[i][j] *= Fraction(value)
    }

    override fun div(i: Int, j: Int, value: Double) {
        values[i][j] /= Fraction(value)
    }


    class Fraction {
        var numerator = BigInteger.ONE
        var denominator = BigInteger.ONE

        constructor()

        constructor(value: Double) {
            valueOf(value)
        }

        private fun gcd(a0: BigInteger, b0: BigInteger): BigInteger {
            var a = a0
            var b = b0
            while (b != BigInteger.ZERO) {
                a = b.also { b = a.mod(b) }
            }
            return a
        }

        fun flow() {
            val gcd = gcd(numerator, denominator)
            numerator /= gcd
            denominator /= gcd
        }

        operator fun plus(value: Fraction): Fraction {
            numerator = value.denominator * numerator + value.numerator * denominator
            denominator *= value.denominator
            flow()
            return this
        }

        operator fun minus(value: Fraction): Fraction {
            numerator = value.denominator * numerator - value.numerator * denominator
            denominator *= value.denominator
            flow()
            return this
        }

        operator fun times(value: Fraction): Fraction {
            numerator *= value.numerator
            denominator *= value.denominator
            flow()
            return this
        }

        operator fun div(value: Fraction): Fraction {
            numerator *= value.denominator
            denominator *= value.numerator
            flow()
            return this
        }

        fun valueOf(value: Double) {
            val s = value.toString()
            if (s.contains('.')) {
                val sp = s.split('.')
                numerator = BigInteger(sp[0] + sp[1])
                denominator = BigInteger.TEN.pow(sp[1].length)
                flow()
            } else {
                numerator = BigInteger(s)
            }
        }

        fun toDouble(): Double = (numerator / denominator).toDouble()

    }
}