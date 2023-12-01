package vp.aoc.day1

import vp.aoc.common.FileLoader

fun main() {
    val numbers = listOf(
        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    )

    val regex = Regex("(?=(${numbers.joinToString("|")}|\\d))")

    FileLoader.loadFile("inputs/day1/input.txt")
        .readLines()
        .map { regex.findAll(it) }
        .map { matches ->
            matches
                .map { checkNotNull(it.groups[1]?.value) }
                .map { it.toLongOrNull() ?: (numbers.indexOf(it) + 1L) }
                .toList()
        }
        .sumOf { it.first() * 10 + it.last() }
        .let { print(it) }
}
