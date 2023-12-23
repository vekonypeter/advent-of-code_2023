package vp.aoc.day9

import vp.aoc.common.FileLoader
import vp.aoc.common.println

fun main() {
    val diffLists = FileLoader.loadFile("inputs/day9/input.txt")
        .readLines()
        .map { it.split(" ").map { s -> s.toLong() } }
        .map { it.diffs() }

    diffLists
        .sumOf { it.extrapolateRight() }
        .println()

    diffLists
        .sumOf { it.extrapolateLeft() }
        .println()
}

private fun List<Long>.diffs(): List<List<Long>> {
    val diffs = mutableListOf(this)

    var last = diffs.last()
    while (last.any { it != last[0] }) {
        diffs.add((1 until last.size).map { last[it] - last[it - 1] })
        last = diffs.last()
    }

    return diffs
}

private fun List<List<Long>>.extrapolateRight(): Long = dropLast(1)
    .foldRight(last()[0]) { curr, acc -> curr.last() + acc }

private fun List<List<Long>>.extrapolateLeft(): Long = dropLast(1)
    .foldRight(last()[0]) { curr, acc -> curr.first() - acc }

