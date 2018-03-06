import java.io.File
import java.io.PrintWriter
import java.util.Arrays

fun main(args: Array<String>) {
    val dir = File("res/double")
    val n = 10
    val d1 = PrintWriter(File(dir, "d1"))
    val d2 = PrintWriter(File(dir, "d2"))
    val d3 = PrintWriter(File(dir, "d3"))
    for (i in 0 until n) {
        for (j in 0 until n) {
            //d2
            val a = (Math.random() * 100).toInt()
            val b = (Math.random() * 100).toInt()
            d2.print("$a.")
        }
    }
}

