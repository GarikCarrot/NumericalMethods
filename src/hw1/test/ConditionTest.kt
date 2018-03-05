package hw1.test

import hw1.ConditionNumber
import hw1.FractionMatrix
import hw1.MatrixReader
import java.io.File

private fun getMatrix(file: String): FractionMatrix = MatrixReader.getFractionMatrix(File("res/fraction/$file.txt"))

fun main(args: Array<String>) {
    val a = getMatrix("fraction1")
    println(ConditionNumber.getCondition(a))
    println(ConditionNumber.getCondition(a.invert()))
    println(ConditionNumber.getCondition(a) * ConditionNumber.getCondition(a.invert()))
}