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
                values[i][j] += (if (isFraction) {
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
                values[i][j] -= (if (isFraction) {
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
                    nValues[i][j] += (if (isFraction) {
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

    fun determinantFraction(): Fraction = subMatrix().determinant()

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

    class Fraction {
        private val numerator: BigInteger
        private val denominator: BigInteger

        private fun toNormalForm(n: BigInteger, d: BigInteger): Pair<BigInteger, BigInteger> {
            var gcd = gcd(n, d)
            if (d < BigInteger.ZERO) gcd = -gcd
            return n / gcd to d / gcd
        }

        constructor(numerator: BigInteger, denominator: BigInteger) {
            val normal = toNormalForm(numerator, denominator)
            this.numerator = normal.first
            this.denominator = normal.second
        }

        constructor() : this(BigInteger.ZERO, BigInteger.ONE)

        constructor(fraction: Fraction) : this(fraction.numerator, fraction.denominator)

        constructor(num: String, denom: String) : this(BigInteger(num), BigInteger(denom))

        constructor(value: Double) {
            val s = value.toString()
            val nnn: BigInteger
            val den: BigInteger
            when {
                s.contains('.') -> {
                    val sp = s.split('.')
                    nnn = BigInteger(sp[0] + sp[1])
                    den = BigInteger.TEN.pow(sp[1].length)
                }
                else -> {
                    nnn = BigInteger(s)
                    den = BigInteger.ONE
                }
            }

            val normal = toNormalForm(nnn, den)
            this.numerator = normal.first
            this.denominator = normal.second
        }

        private fun gcd(a0: BigInteger, b0: BigInteger): BigInteger {
            var a = a0.abs()
            var b = b0.abs()
            if (b > a) a = b.also { b = a }
            while (b != BigInteger.ZERO) {
                a = b.also { b = a.mod(b) }
            }
            return a
        }

        operator fun plus(value: Fraction): Fraction =
                Fraction(value.denominator * numerator + value.numerator * denominator,
                        denominator * value.denominator)

        operator fun minus(value: Fraction): Fraction = Fraction(
                value.denominator * numerator - value.numerator * denominator,
                denominator * value.denominator)

        operator fun times(value: Fraction): Fraction = Fraction(
                numerator * value.numerator,
                denominator * value.denominator)

        operator fun div(value: Fraction): Fraction = Fraction(
                numerator * value.denominator,
                denominator * value.numerator)

        operator fun unaryMinus(): Fraction = Fraction(-numerator, denominator)


        fun toDouble(): Double = (numerator / denominator).toDouble()

        override fun toString(): String = numerator.toString() + "/" + denominator.toString()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Fraction

            if (numerator != other.numerator) return false
            if (denominator != other.denominator) return false

            return true
        }

        override fun hashCode(): Int {
            var result = numerator.hashCode()
            result = 31 * result + denominator.hashCode()
            return result
        }
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
                result +=
                        if (i % 2 == 0) get(i, 0) * d
                        else -get(i, 0) * d
            }
            return result
        }
    }

}