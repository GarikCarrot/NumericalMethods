package hw1

import java.math.BigInteger

class FractionMatrix(private var n: Int, private var m: Int) : Matrix {
    private var values = Array(n) { Array(m) { Fraction() } }

    override fun plus(matrix: Matrix): Matrix = assign { it += matrix }

    override fun minus(matrix: Matrix): Matrix = assign { it -= matrix }

    override fun times(matrix: Matrix): Matrix = assign { it *= matrix }

    private fun assign(v: (FractionMatrix) -> Unit): FractionMatrix {
        val result = FractionMatrix(n, m)
        result += this
        v(result)
        return result
    }

    override fun plusAssign(matrix: Matrix) {
        val isFraction = matrix is FractionMatrix
        for (i in 0 until n) {
            for (j in 0 until m) {
                values[i][j].plusAssign(if (isFraction) {
                    (matrix as FractionMatrix).values[i][j]
                } else {
                    Fraction(matrix.get(i, j))
                })
            }
        }
    }

    override fun minusAssign(matrix: Matrix) {
        val isFraction = matrix is FractionMatrix
        for (i in 0 until n) {
            for (j in 0 until m) {
                values[i][j].minusAssign(if (isFraction) {
                    (matrix as FractionMatrix).values[i][j]
                } else {
                    Fraction(matrix.get(i, j))
                })
            }
        }
    }

    override fun timesAssign(matrix: Matrix) {
        val nM = matrix.size().second
        if (size().second != matrix.size().first) throw MatrixSizeException()
        val isFraction = matrix is FractionMatrix
        val nValues = Array(n) { Array(nM) { Fraction() } }
        for (i in 0 until n) {
            for (j in 0 until m) {
                for (k in 0 until nM) {
                    nValues[i][j].plusAssign(if (isFraction) {
                        values[i][k] * (matrix as FractionMatrix).values[k][j]
                    } else {
                        values[i][k] * Fraction(matrix.get(k, j))
                    })
                }
            }
        }
        values = nValues
    }

    override fun trans(): Matrix {
        val result = FractionMatrix(m, n)
        for (i in 0 until n) {
            for (j in 0 until m) {
                result.values[j][i] = values[i][j]
            }
        }
        return result
    }

    override fun invert(): Matrix {
        if (n != m) throw MatrixSizeException()
        val result = FractionMatrix(n, m)
        val d = determinantFraction()
        for (i in 0 until n) {
            for (j in 0 until m) {
                val value = subMatrix(i, j).determinant() / d
                if ((i + j) % 2 == 0) {
                    result.values[i][j] = value
                } else {
                    result.values[i][j] = -value
                }
            }
        }
        return result.trans()
    }

    override fun set(i: Int, j: Int, value: Double) {
        values[i][j] = Fraction(value)
    }

    fun setFraction(i: Int, j: Int, value: Fraction) {
        values[i][j] = value
    }

    fun getFraction(i: Int, j: Int): Fraction = Fraction(values[i][j])

    override fun get(i: Int, j: Int): Double = values[i][j].toDouble()

    override fun change(i: Int, j: Int, change: (Double) -> Double) {
        set(i, j, change(get(i, j)))
    }

    override fun determinant(): Double = determinantFraction().toDouble()

    fun determinantFraction():Fraction = subMatrix().determinant()

    override fun size(): Pair<Int, Int> = Pair(n, m)

    override fun equals(other: Any?): Boolean {
        if (other !is FractionMatrix) return false
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (getFraction(i, j) != other.getFraction(i, j))
                    return false
            }
        }
        return true
    }

    public class Fraction {
        private var numerator = BigInteger.ZERO
        private var denominator = BigInteger.ONE

        constructor()

        constructor(fraction: Fraction) {
            numerator = fraction.numerator
            denominator = fraction.denominator
        }

        constructor(num: String, denom: String) {
            numerator = BigInteger(num)
            denominator = BigInteger(denom)
        }

        constructor(value: Double) {
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

        operator fun plus(value: Fraction): Fraction = assign { it += value }

        operator fun minus(value: Fraction): Fraction = assign { it -= value }

        operator fun times(value: Fraction): Fraction = assign { it *= value }

        operator fun div(value: Fraction): Fraction = assign { it /= value }

        operator fun unaryMinus() : Fraction {
            val result = Fraction(this)
            result.numerator = -result.numerator
            return result
        }

        private fun assign(v: (Fraction) -> Unit): Fraction {
            val result = Fraction(this)
            v(result)
            return result
        }

        operator fun plusAssign(value: Fraction) {
            numerator = value.denominator * numerator + value.numerator * denominator
            denominator *= value.denominator
            flow()
        }

        operator fun minusAssign(value: Fraction) {
            numerator = value.denominator * numerator - value.numerator * denominator
            denominator *= value.denominator
            flow()
        }

        operator fun timesAssign(value: Fraction) {
            numerator *= value.numerator
            denominator *= value.denominator
            flow()
        }

        operator fun divAssign(value: Fraction) {
            numerator *= value.denominator
            denominator *= value.numerator
            flow()
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Fraction) return false
            flow()
            other.flow()
            return numerator == other.numerator && denominator == other.denominator
        }

        fun toDouble(): Double = (numerator / denominator).toDouble()

        override fun toString(): String = numerator.toString() + "/" + denominator.toString()
    }

    private fun subMatrix(): SubMatrix = SubMatrix(this, n, n, n)

    private fun subMatrix(wx: Int, wy: Int): SubMatrix = SubMatrix(this, wx, wy, n - 1)


    private class SubMatrix {
        private val parentM: FractionMatrix?
        private val parent: SubMatrix?
        private val wx: Int
        private val wy: Int
        private val size: Int

        constructor(parent: FractionMatrix, wx: Int, wy: Int, size: Int) {
            this.parentM = parent
            this.parent = null
            this.wx = wx
            this.wy = wy
            this.size = size
        }

        constructor(parent: SubMatrix, wx: Int, wy: Int, size: Int) {
            this.parentM = null
            this.parent = parent
            this.wx = wx
            this.wy = wy
            this.size = size
        }

        private fun get(i: Int, j: Int): Fraction {
            val x = if (i < wx) i else i + 1
            val y = if (j < wy) j else j + 1
            return parentM?.getFraction(x, y) ?: parent!!.get(x, y)
        }

        fun determinant(): Fraction {
            if (size == 1) return get(0, 0)
            var result = Fraction()
            for (i in 0 until size) {
                val d = SubMatrix(this, i, 0, size - 1).determinant()
                if (i % 2 == 0) result = result + get(i, 0) * d
                else result = result - get(i, 0) * d
            }
            return result
        }
    }

}