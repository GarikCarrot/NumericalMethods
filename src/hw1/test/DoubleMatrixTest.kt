package hw1.test

import hw1.DoubleMatrix
import hw1.MatrixReader
import java.io.File

fun getMatrix(file: String): DoubleMatrix = MatrixReader.getDoubleMatrix(File("res/double/$file.txt"))

fun main(args: Array<String>) {
    val a = getMatrix("double1")
    val b = getMatrix("double2")
    val sum = getMatrix("sum")
    val sub = getMatrix("sub")
    val mul = getMatrix("mul")
    val trans = getMatrix("trans")
    val invert = getMatrix("invert")
    assert(a + b == sum)
    assert(a - b == sub)
    assert(a * b == mul)
    assert(a.trans() == trans)
}
