package hw1.test

import hw1.ConditionNumber
import hw1.DoubleMatrix
import hw1.FractionMatrix
import hw1.MatrixReader
import java.io.File

private fun getMatrix(file: String): DoubleMatrix = MatrixReader.getDoubleMatrix(File("res/double/$file.txt"))

fun main(args: Array<String>) {
    val a = getMatrix("double1")
    println(ConditionNumber.getCondition(a))
}