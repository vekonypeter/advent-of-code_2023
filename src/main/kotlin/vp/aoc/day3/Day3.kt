package vp.aoc.day3

import vp.aoc.common.FileLoader

fun main() {
    val notations = mutableListOf<Notation>()
    val symbols = mutableListOf<Symbol>()

    val numRegex = Regex("\\d+")
    val symRegex = Regex("[^.,\\w]")
    FileLoader.loadFile("inputs/day3/input.txt")
        .readLines()
        .forEachIndexed { y, line ->
            numRegex.findAll(line)
                .map { checkNotNull(it.groups[0]) }
                .forEach { notations.add(Notation(it.value.toInt(), it.range, y)) }
            symRegex.findAll(line)
                .map { checkNotNull(it.groups[0]) }
                .forEach { symbols.add(Symbol(it.value[0], Cords(it.range.first, y))) }
        }

    val symbolsCords = symbols.map { it.cords }
    notations
        .filter { it.adjCords.any { c -> symbolsCords.contains(c) } }
        .sumOf { it.number }
        .let { println(it) }

    symbols
        .filter { it.symbol == '*' }
        .map { notations.filter { n -> n.adjCords.contains(it.cords) } }
        .filter { it.size == 2 }
        .sumOf { it[0].number * it[1].number }
        .let { println(it) }
}

data class Notation(
    val number: Int,
    val x: IntRange,
    val y: Int,
) {
    val adjCords: List<Cords> = (x.first - 1..x.last + 1)
        .map { listOf(Cords(it, y - 1), Cords(it, y), Cords(it, y + 1)) }
        .flatten()
}

data class Symbol(
    val symbol: Char,
    val cords: Cords,
)

data class Cords(
    val x: Int,
    val y: Int,
)
