package vp.aoc.day6

import vp.aoc.common.FileLoader

fun main() {

    val input = FileLoader.loadFile("inputs/day6/input.txt")
        .readLines()

    Race.parsePart1(input[0], input[1])
        .map { it.getBetterChargeTimes().size }
        .reduce { acc, i -> acc * i }
        .let { println(it) }

    Race.parsePart2(input[0], input[1])
        .getBetterChargeTimes()
        .size
        .let { println(it) }
}

data class Race(
    val time: Long,
    val currentRecordDist: Long,
) {
    fun getBetterChargeTimes(): List<Long> =
        (0..time).filter { calcDistance(it) > currentRecordDist }

    private fun calcDistance(chargeTime: Long) =
        (time - chargeTime) * chargeTime

    companion object {
        private val numRegex = Regex("\\d+")

        fun parsePart1(timeLine: String, distLine: String): List<Race> {
            val timeMatches = numRegex.findAll(timeLine).toList()
            val distMatches = numRegex.findAll(distLine).toList()

            return timeMatches.mapIndexed { idx, timeMatch ->
                Race(timeMatch.value.toLong(), distMatches[idx].value.toLong())
            }
        }

        fun parsePart2(timeLine: String, distLine: String) = Race(
            numRegex.findAll(timeLine).mergeToLong(),
            numRegex.findAll(distLine).mergeToLong()
        )

        private fun Sequence<MatchResult>.mergeToLong() = joinToString("") { it.value }.toLong()
    }
}
