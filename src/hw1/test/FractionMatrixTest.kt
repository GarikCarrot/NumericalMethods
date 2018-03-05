package hw1.test

import hw1.FractionMatrix
import hw1.MatrixReader
import java.io.File

private fun getMatrix(file: String): FractionMatrix = MatrixReader.getFractionMatrix(File("res/fraction/$file.txt"))

fun main(args: Array<String>) {
    val a = getMatrix("fraction1")
    val b = getMatrix("fraction2")
    val sum = getMatrix("sum")
    val sub = getMatrix("sub")
    val mul = getMatrix("mul")
    val trans = getMatrix("trans")
    val invert = getMatrix("invert")
    val ia = a.invert()
    assert(a + b == sum)
    assert(a - b == sub)
    assert(a * b == mul)
    assert(a.determinant() == FractionMatrix.Fraction(8.0))
    assert(a.trans() == trans)
    assert(a.invert() == invert)
}