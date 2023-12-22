package vp.aoc.day8

import vp.aoc.common.FileLoader
import vp.aoc.common.println
import kotlin.math.min

fun main() {
    val regex = Regex("^(\\w+) = \\((\\w+), (\\w+)\\)\$")

    val lines = FileLoader.loadFile("inputs/day8/input.txt")
        .readLines()

    val instructions = lines[0]

    val nodes = lines.subList(2, lines.size)
        .associate { line ->
            val groups = checkNotNull(regex.find(line)).groups
            groups[1]!!.value to (groups[2]!!.value to groups[3]!!.value)
        }

// Does not work with example 3.
//
//    var currKey = "AAA"
//    var i = 0
//    while (currKey != "ZZZ") {
//        val instr = instructions.getWithRepeat(i)
//        val curr = nodes[currKey]!!
//        currKey = if (instr == 'L') curr.first else curr.second
//        i++
//    }
//
//    println(i)

    val currKeys = nodes.keys.filter { it.endsWith("A") }

    val repeats = currKeys
        .map {
            var currKey = it
            var j = 0L
            while (!currKey.endsWith("Z")) {
                val instr = instructions.getWithRepeat(j)
                val curr = nodes[currKey]!!
                currKey = if (instr == 'L') curr.first else curr.second
                j++
            }

            j
        }

    repeats.lcm().println()
}

private fun String.getWithRepeat(idx: Long) = this[(idx % length).toInt()]

private fun gcd(a: Long, b: Long): Long {
    val min = min(a, b)

    if (a % min == 0L && b % min == 0L) return min

    (1..min / 2)
        .reversed()
        .forEach { d -> if (a % d == 0L && b % d == 0L) return d }
    return 1
}

private fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

private fun List<Long>.lcm(): Long = reduce { a, b -> lcm(a, b) }
