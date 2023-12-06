package vp.aoc.day4

import vp.aoc.common.FileLoader
import kotlin.math.pow

fun main() {
    val cards = FileLoader.loadFile("inputs/day4/input.txt")
        .readLines()
        .map { Card.parse(it) }

    cards
        .sumOf { it.points() }
        .let { println(it) }

    cards
        .sumOf { it.calcNumOfSubCards(cards) }
        .let { println(it) }
}

data class Card(
    val id: Int,
    val winningNumber: Set<Int>,
    val numbers: Set<Int>,
) {
    private val matches = numbers.intersect(winningNumber).size

    private val subCards = if (matches > 0) (id + 1)..(id + matches) else IntRange.EMPTY

    fun points(): Int = if (matches > 0) 2f.pow(matches.toFloat() - 1).toInt() else 0

    fun calcNumOfSubCards(allCards: List<Card>, acc: Int = 1): Int =
        acc + if (subCards.isEmpty()) 0
        else subCards.sumOf { allCards[it - 1].calcNumOfSubCards(allCards, acc) }

    companion object {

        private val regex = Regex("Card\\s+(\\d+):(.+)\\|(.+)")
        private val whiteSpaceRegex = Regex("\\s+")

        fun parse(line: String): Card {
            val matches = checkNotNull(regex.find(line)?.groups) { "line $line did not match" }

            val id = matches[1]!!.value.toInt()
            val winningNumber = matches[2]!!.value.toIntSet()
            val numbers = matches[3]!!.value.toIntSet()

            return Card(id, winningNumber, numbers)
        }

        private fun String.toIntSet() = trim().split(whiteSpaceRegex).map { it.toInt() }.toSet()
    }
}
