package vp.aoc.day12

import vp.aoc.common.FileLoader
import java.io.File

/**
 * Not my solution, taken from https://github.com/eagely/adventofcode/blob/main/src/main/kotlin/solutions/y2023/Day12.kt
 */
fun main() {
    val file = FileLoader.loadFile("inputs/day12/input.txt")

    val res1 = solvePart1(file)
    println("PART 1: $res1")

    val res2 = solvePart2(file)
    println("PART 2: $res2")
}

private fun <T> Collection<T>.dropBlanks() = this.filter { it.toString().isNotBlank() }

private fun solvePart1(input: File) =
    input.readLines().sumOf { it.split(" ").let { count(it.first(), it[1].split(",").map(String::toInt)) } }

private fun solvePart2(input: File) = input.readLines().sumOf {
    it.split(" ").let {
        count(
            "${it.first()}?".repeat(5).dropLast(1),
            "${it[1]},".repeat(5).split(",").dropBlanks().map(String::toInt)
        )
    }
}

private val cache = hashMapOf<Pair<String, List<Int>>, Long>()

private fun count(config: String, groups: List<Int>): Long {
    if (groups.isEmpty()) return if ("#" in config) 0 else 1
    if (config.isEmpty()) return 0

    return cache.getOrPut(config to groups) {
        var result = 0L
        if (config.first() in ".?")
            result += count(config.drop(1), groups)
        if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
            result += count(config.drop(groups.first() + 1), groups.drop(1))
        result
    }
}
