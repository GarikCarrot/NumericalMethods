package hw1.test

import hw1.DoubleMatrix
import hw1.MatrixReader
import hw1.solvers.ConjugateGradientSolver
import hw1.solvers.GaussianSolver
import hw1.solvers.JacobiSolver
import hw1.solvers.SeidelSolver
import java.io.File

private fun getMatrix(file: String): DoubleMatrix = MatrixReader.getDoubleMatrix(File("res/double/$file.txt"))

fun main(args: Array<String>) {
    val a = getMatrix("double1")
    val b = getMatrix("double3")
    val c = getMatrix("init")
    val result = getMatrix("result")
//    GaussianSolver.getSolve(a, b)
//    JacobiSolver.getSolve(a, b, c)
//    SeidelSolver.getSolve(a, b, c)
    ConjugateGradientSolver.getSolve(a, b, c, 1e-6)
}