package vp.aoc.day10

import vp.aoc.common.FileLoader
import vp.aoc.common.println

fun main() {
    val map = FileLoader.loadFile("inputs/day10/ex1.txt")
        .readLines()

    val sy = map.indexOfFirst { it.contains("S") }
    val sx = map[sy].indexOf("S")
    val s = Cords(sx, sy)

    operator fun List<String>.get(cords: Cords) = this[cords.y][cords.x]
    operator fun List<String>.get(x: Int, y: Int) = this[y][x]

    fun Cords.next(prev: Cords): Cords = when (map[this]) {
        'L' -> listOf(Cords(x, y - 1), Cords(x + 1, y))
        '-' -> listOf(Cords(x - 1, y), Cords(x + 1, y))
        '|' -> listOf(Cords(x, y - 1), Cords(x, y + 1))
        'J' -> listOf(Cords(x, y - 1), Cords(x - 1, y))
        'F' -> listOf(Cords(x + 1, y), Cords(x, y + 1))
        '7' -> listOf(Cords(x - 1, y), Cords(x, y + 1))
        else -> throw RuntimeException("Unexpected symbol ${map[this]} at $this")
    }.first { it != prev }

    fun Cords.next(): Cords = when {
        map[x, y - 1] in listOf('|', '7', 'F') -> Cords(x, y - 1)
        map[x + 1, y] in listOf('-', '7', 'J') -> Cords(x + 1, y)
        map[x, y + 1] in listOf('|', 'J', 'L') -> Cords(x, y + 1)
        map[x - 1, y] in listOf('-', 'F', 'L') -> Cords(x - 1, y)
        else -> throw RuntimeException("Nowhere to go from $this")
    }

    val path = mutableListOf(s, s.next())

    while (path.last() != s) {
        val lastTwo = path.takeLast(2)
        path.add(lastTwo.last().next(lastTwo.first()))
    }
    ((path.size - 1) / 2).println()


    var area = 0
    for (i in 0 until path.size - 1) {
        val a = path[i]
        val b = path[i + 1]

        area += a.x * b.y - a.y * b.x
    }

//    path.println()
//
//    path.size.println()
//    area.println()
//    (area / 2).println()
}

data class Cords(
    val x: Int,
    val y: Int,
)
