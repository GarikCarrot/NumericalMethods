package hw1

import java.math.BigInteger

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
