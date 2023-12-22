package vp.aoc.day7

import vp.aoc.common.FileLoader
import vp.aoc.common.println

fun main() {

    val cards = FileLoader.loadFile("inputs/day7/input.txt")
        .readLines()
        .map { Hand.parse(it) }

    cards
        .sortedWith(Hand::compare1)
        .mapIndexed { i, hand -> hand.bid * (i + 1) }
        .sum()
        .let { it.println() }

    cards
        .sortedWith(Hand::compare2)
        .mapIndexed { i, hand -> hand.bid * (i + 1) }
        .sum()
        .let { it.println() }
}

data class Hand(
    val cards: String,
    val bid: Long,
) {
    val chars: Map<Char, Int> = cards.toCharArray()
        .groupBy { it }
        .mapValues { it.value.size }

    val nonJokerChars: Map<Char, Int> = cards.toCharArray()
        .filterNot { it == 'J' }
        .groupBy { it }
        .mapValues { it.value.size }


    val jokers: Int = chars['J'] ?: 0

    private val type1 = HandType.values().first { it.criteria1(this) }
    private val type2 = HandType.values().first { it.criteria1(this) || it.criteria2(this) }

    fun compare1(other: Hand): Int =
        if (type1.strength > other.type1.strength) 1
        else if (type1.strength < other.type1.strength) -1
        else {
            for (i in cards.indices) {
                val r1 = ranking1.indexOf(cards[i])
                val r2 = ranking1.indexOf(other.cards[i])
                r1.compareTo(r2).let { if (it != 0) return it }
            }
            0
        }

    fun compare2(other: Hand): Int =
        if (type2.strength > other.type2.strength) 1
        else if (type2.strength < other.type2.strength) -1
        else {
            for (i in cards.indices) {
                val r1 = ranking2.indexOf(cards[i])
                val r2 = ranking2.indexOf(other.cards[i])
                r1.compareTo(r2).let { if (it != 0) return it }
            }
            0
        }

    companion object {
        private val ranking1 = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
        private val ranking2 = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

        private val lineRegex = Regex("^(.+) (\\d+)\$")

        fun parse(line: String): Hand =
            requireNotNull(lineRegex.find(line))
                .let { Hand(it.groups[1]!!.value, it.groups[2]!!.value.toLong()) }
    }
}

enum class HandType(
    val strength: Int,
    val criteria1: (Hand) -> Boolean,
    val criteria2: (Hand) -> Boolean,
) {
    FIVE_OF_KIND(
        6,
        { it.chars.containsValue(5) },
        { it.nonJokerChars.values.max() + it.jokers == 5 }
    ),
    FOUR_OF_KIND(
        5,
        { it.chars.containsValue(4) },
        { it.nonJokerChars.values.max() + it.jokers == 4 },
    ),
    FULL_HOUSE(
        4,
        { it.chars.containsValue(3) && it.chars.containsValue(2) },
        { it.nonJokerChars.values.count { v -> v == 2 } == 2 && it.jokers == 1 },
    ),
    THREE_OF_KIND(
        3,
        { it.chars.containsValue(3) },
        { it.nonJokerChars.values.max() + it.jokers == 3 },
    ),
    TWO_PAIRS(
        2,
        { it.chars.values.count { v -> v == 2 } == 2 },
        { false },
    ),
    PAIR(
        1,
        { it.chars.containsValue(2) },
        { it.nonJokerChars.values.max() + it.jokers == 2 },
    ),
    HIGH(0, { true }, { true });
}
