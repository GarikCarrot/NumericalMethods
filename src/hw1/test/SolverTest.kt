package hw1.test

import hw1.DoubleMatrix
import hw1.MatrixReader
import hw1.solvers.ConjugateGradientSolver
import hw1.solvers.GaussianSolver
import hw1.solvers.JacobiSolver
import hw1.solvers.SeidelSolver
import java.io.File

private fun getMatrix(file: String): DoubleMatrix = MatrixReader.getDoubleMatrix(File("res/double/$file.txt"))

private fun printResult(matrix: DoubleMatrix) {
    for (i in 0 until matrix.size().first) {
        print(matrix.get(i, 0).toString() + " ")
    }
    println()
}

fun main(args: Array<String>) {
    val a = getMatrix("double1")
    val b = getMatrix("double3")
    val c = getMatrix("init")
    printResult(GaussianSolver.getSolve(a, b))
    printResult(JacobiSolver.getSolve(a, b, c))
    printResult(SeidelSolver.getSolve(a, b, c))
    printResult(ConjugateGradientSolver.getSolve(a, b, c, 1e-6))
}