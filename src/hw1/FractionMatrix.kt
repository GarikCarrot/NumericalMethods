package hw1

import java.math.BigInteger

class FractionMatrix : Matrix<FractionMatrix, FractionMatrix.Fraction> {

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

    private val n: Int
    private val m: Int

    private val values: Array<Array<Fraction>>

    constructor(n: Int, m: Int, values: Array<Array<Fraction>>) {
        this.n = n
        this.m = m
        this.values = values
    }

    constructor(n: Int, m: Int) : this(n, m, Array(n, { Array(m, { _ -> Fraction() }) }));


    private fun apply(matrix: FractionMatrix, function: (Pair<Fraction, Fraction>) -> Fraction): FractionMatrix {
        if (n != matrix.n || m != matrix.m) throw MatrixSizeException()

        return FractionMatrix(n, m, values.zip(matrix.values).map {
            it.first.zip(it.second).map {
                function(it)
            }.toTypedArray()
        }.toTypedArray())
    }

    override fun plus(matrix: FractionMatrix): FractionMatrix = apply(matrix) { it.first + it.second }

    override fun minus(matrix: FractionMatrix): FractionMatrix = apply(matrix) { it.first - it.second }

    override fun times(matrix: FractionMatrix): FractionMatrix {
        if (m != matrix.n || n != matrix.m) throw MatrixSizeException()

        val newM = matrix.m

        val newValues: Array<Array<Fraction>> = Array(n) { Array(newM) { Fraction() } }
        (0 until n).forEach { i ->
            (0 until m).forEach { j ->
                (0 until newM).forEach { k ->
                    newValues[i][j] += values[i][k] * matrix.values[k][j]
                }
            }
        }
        return FractionMatrix(n, newM, newValues)
    }

    override fun trans(): FractionMatrix {
        val newValues: Array<Array<Fraction>> = Array(n) { Array(m) { Fraction() } }

        (0 until n).forEach { i ->
            (0 until m).forEach { j -> newValues[j][i] = values[i][j] }
        }
        return FractionMatrix(m, n, newValues)
    }

    override fun invert(): FractionMatrix {
        if (n != m) throw MatrixSizeException()
        val newValues: Array<Array<Fraction>> = Array(n) { Array(m) { Fraction() } }

        val d = determinant()
        (0 until n).forEach { i ->
            (0 until m).forEach { j ->
                val value = subMatrix(i, j).determinant() / d
                if ((i + j) % 2 == 0) {
                    newValues[i][j] = value
                } else {
                    newValues[i][j] = -value
                }
            }
        }

        return FractionMatrix(n, m, newValues).trans()
    }

    fun clone(): FractionMatrix = FractionMatrix(n, m, values.map { it.copyOf() }.toTypedArray())

    override operator fun set(i: Int, j: Int, value: Fraction) { values[i][j] = value }

    override operator fun get(i: Int, j: Int): Fraction = Fraction(values[i][j])

    override fun change(i: Int, j: Int, change: (Fraction) -> Fraction) = set(i, j, change(get(i, j)))

    override fun determinant(): Fraction = subMatrix().determinant()

    override fun size(): Pair<Int, Int> = Pair(n, m)

    override fun equals(other: Any?): Boolean {
        if (other !is FractionMatrix) return false
        for (i in 0 until n) {
            for (j in 0 until m) {
                if (this[i, j] != other[i, j])
                    return false
            }
        }
        return true
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
            return parentM?.get(x, y) ?: parent!!.get(x, y)
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