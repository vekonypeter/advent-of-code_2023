package vp.aoc.day11

import vp.aoc.common.FileLoader
import java.lang.Math.max
import java.lang.Math.min
import kotlin.math.abs

fun main() {
    val map = FileLoader.loadFile("inputs/day11/input.txt")
        .readLines()

    val emptyRows = map.indices.filter { y -> map[y].all { it == '.' } }
    val emptyCols = map[0].indices.filter { x -> map.all { it[x] == '.' } }

    val galaxies = buildList {
        for (y in map.indices)
            for (x in map[y].indices)
                if (map[y][x] == '#') add(Cords(x, y))
    }

    val pairs = buildSet {
        for (a in galaxies)
            for (b in galaxies)
                if (a != b && !contains(b to a)) add(a to b)
    }

    fun Cords.simpleDistanceTo(other: Cords): Long = abs(x - other.x).toLong() + abs(y - other.y)

    fun Cords.expandedDistanceTo(other: Cords, emptyDistance: Int = 1): Long {
        val emptyRowsBetween = emptyRows.count { it in min(y, other.y) until max(y, other.y) }
        val emptyColsBetween = emptyCols.count { it in min(x, other.x) until max(x, other.x) }
        return (emptyRowsBetween + emptyColsBetween) * emptyDistance + simpleDistanceTo(other)
    }

    val res1 = pairs
        .sumOf { it.first.expandedDistanceTo(it.second) }

    println("PART 1: $res1")

    val res2 = pairs
        .sumOf { it.first.expandedDistanceTo(it.second, emptyDistance = 1000000 - 1) }

    println("PART 2: $res2")
}

private data class Cords(
    val x: Int,
    val y: Int,
)
