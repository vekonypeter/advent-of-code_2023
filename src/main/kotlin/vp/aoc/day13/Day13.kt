package vp.aoc.day13

import vp.aoc.common.FileLoader

fun main() {
    val maps = FileLoader.loadFile("inputs/day13/input.txt")
        .readLines()
        .fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) acc.add(mutableListOf())
            else acc.last().add(line)
            acc
        }

    val res1 = maps
        .map {
            val hor = it.countRowsAboveReflection()
            val ver = if (hor == 0) it.countColsLeftToReflection() else 0
            ver to hor
        }
        .sumPairs().summarizeNotes()
    println("PART 1: $res1")

    val res2 = maps
        .map {
            val hor = it.countRowsAboveReflectionAfterChange()
            val ver = if (hor == 0) it.countColsLeftToReflectionAfterChange() else 0
            ver to hor
        }
        .sumPairs().summarizeNotes()
    println("PART 2: $res2")
}

private fun List<String>.countColsLeftToReflection(): Int =
    rotateRight().countRowsAboveReflection()

private fun List<String>.countRowsAboveReflection(): Int {
    val possibleStarts = this
        .mapIndexedNotNull { i, line -> if (i < this.size - 1 && line == this[i + 1]) i else null }

    return possibleStarts.find { reflectsFrom(it) }?.let { it + 1 } ?: 0
}

private fun List<String>.reflectsFrom(startUp: Int): Boolean {
    val startDown = startUp + 1
    var i = 0
    while (startUp - i >= 0 && startDown + i < this.size) {
        if (this[startUp - i] != this[startDown + i]) return false
        i++
    }
    return true
}

private fun List<String>.countColsLeftToReflectionAfterChange(): Int =
    rotateRight().countRowsAboveReflectionAfterChange()

private fun List<String>.countRowsAboveReflectionAfterChange(): Int {
    for (i in indices) {
        if (hasOneDiffReflectingFrom(i) == 1) return i + 1
    }
    return 0
}

private fun List<String>.hasOneDiffReflectingFrom(startUp: Int): Int {
    val startDown = startUp + 1
    var i = 0
    var sum = 0
    while (startUp - i >= 0 && startDown + i < this.size) {
        sum += this[startUp - i].diffTo(this[startDown + i])
        i++
    }
    return sum
}

private fun String.diffTo(other: String): Int = this.zip(other).count { (a, b) -> a != b }

private fun List<String>.rotateRight(): List<String> {
    val newMap = MutableList(first().length) { "" }
    forEach { line ->
        line.forEachIndexed { i, c ->
            newMap[i] = c + newMap[i]
        }
    }
    return newMap
}

private fun Collection<Pair<Int, Int>>.sumPairs() = reduce { (vAcc, hAcc), (vCur, hCur) -> vAcc + vCur to hAcc + hCur }
private fun Pair<Int, Int>.summarizeNotes() = first + 100 * second
