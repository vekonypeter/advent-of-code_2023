package vp.aoc.day2

import vp.aoc.common.FileLoader
import vp.aoc.day2.Color.*

fun main() {
    val constraints = mapOf(
        RED to 12,
        GREEN to 13,
        BLUE to 14,
    )

    val games = FileLoader.loadFile("inputs/day2/input.txt")
        .readLines()
        .map { Game.parse(it) }

    games
        .filter { it.sets.all { set -> set.isPossible(constraints) } }
        .sumOf { it.id }
        .let { println(it) }

    games
        .sumOf { it.minSet.power() }
        .let { println(it) }
}

data class Game(
    val id: Int,
    val sets: List<Map<Color, Int>>
) {
    val minSet = values().associateWith { color -> sets.maxOf { it[color] ?: 0 } }

    companion object {
        fun parse(line: String) = Regex("Game (\\d+):(.+)")
            .find(line)
            .let { res ->
                res!!
                val gameId = res.groups[1]!!.value.toInt()
                val allSets = res.groups[2]!!.value

                val sets = allSets.split(";")
                    .map { set ->
                        Regex("(\\d+) (\\w+)")
                            .findAll(set)
                            .map { it.groups[2]!!.value.toColor() to it.groups[1]!!.value.toInt() }
                            .toMap()
                    }
                Game(gameId, sets)
            }
    }
}

enum class Color {
    RED,
    GREEN,
    BLUE
}

fun String.toColor() = Color.valueOf(this.uppercase())

fun Map<Color, Int>.isPossible(constraints: Map<Color, Int>) =
    this.keys
        .filter { constraints.containsKey(it) }
        .all { this[it]!! <= constraints[it]!! }

fun Map<Color, Int>.power() = this.values.reduce { acc, i -> acc * i}
