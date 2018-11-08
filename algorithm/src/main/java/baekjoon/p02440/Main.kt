package baekjoon.p02440

fun main(args: Array<String>) {
    val n = readLine()!!.toInt()
    for (ret in solve(n)) {
        println(ret)
    }
}

private fun solve(n: Int): Array<String> {
    val ret = Array(n) { "" }
    val size = ret.size

    for (i in 0 until size) {
        ret[i] = "*".repeat(size - i)
    }

    return ret
}