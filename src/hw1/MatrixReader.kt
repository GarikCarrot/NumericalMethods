package hw1

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.stream.Collectors

class MatrixReader {
    companion object {
        fun getDoubleMatrix(file: File): DoubleMatrix {
            val input = BufferedReader(FileReader(file))
            val lines = input.lines().collect(Collectors.toList())
            val n = lines.size
            val m = lines[0].split("\\s+".toRegex()).size
            val matrix = DoubleMatrix(n, m)
            for (i in 0 until n) {
                val s = lines[i].split("\\s+".toRegex())
                for (j in 0 until m) {
                    matrix.set(i, j, s[j].toDouble())
                }
            }
            return matrix
        }
    }

}