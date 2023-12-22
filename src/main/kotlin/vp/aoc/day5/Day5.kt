package vp.aoc.day5

import vp.aoc.common.FileLoader

fun main() {
    val input = FileLoader.loadFile("inputs/day5/input.txt") // add double newline to end of file!
        .readText()

    val seeds = Regex("seeds: (.+)").find(input)!!.groups[1]!!.value.split(" ").map { it.toLong() }

    val maps = Regex("(\\w+)-to-(\\w+) map:\\n((.|\\n)+?)(\\n\\n)").findAll(input)
        .map { AlmanacMap.parse(it) }

    seeds.minOfOrNull { maps.fold(it) { current, map -> map.fromTo(current) } }
        .let { println(it) }

    val seedRanges = seeds.chunked(2)
        .map { it[0] until it[0] + it[1] }

    // takes forever with real input:
    //
    // seedRanges
    //     .minOf { seedRange ->
    //         seedRange
    //             .minOf { maps.fold(it) { current, map -> map.fromTo(current) } }
    //     }
    //     .let { println(it) }

    seedRanges.map { maps.fold(it) { current, map -> map.fromTo(current) } }
        .let { println(it) }
}

data class AlmanacMap(
    val from: String,
    val to: String,

    val instructions: List<Instruction>,
) {

    fun fromTo(value: Long): Long = instructions.firstNotNullOfOrNull { it.transfer(value) } ?: value

    fun fromTo(values: LongRange): LongRange = instructions.firstNotNullOfOrNull { it.transfer(values) } ?: values

    companion object {

        private val lineRegex = Regex("(\\d+) (\\d+) (\\d+)")

        fun parse(match: MatchResult): AlmanacMap {
            val from = match.groups[1].asString()
            val to = match.groups[2].asString()

            val instructions = match.groups[3].asString().split("\n")
                .map {
                    lineRegex.find(it)!!.groups.let { groups ->
                        Instruction(
                            srcFrom = groups[2].asLong(),
                            dstFrom = groups[1].asLong(),
                            length = groups[3].asLong(),
                        )
                    }
                }

            return AlmanacMap(from, to, instructions)
        }
    }
}

data class Instruction(
    val srcFrom: Long,
    val dstFrom: Long,
    val length: Long,
) {
    private val srcRange = srcFrom..(srcFrom + length)
    private val dstRange = dstFrom..(dstFrom + length)

    fun transfer(value: Long): Long? =
        if (value in srcRange) dstFrom + (value - srcFrom) else null

    fun transfer(values: LongRange): LongRange? {
        val common = srcRange.intersect(values)

        return if (common.isEmpty()) null
        else {
            val startIdx = srcRange.indexOf(common.min())
            val endIdx = srcRange.indexOf(common.max())

            val dstRes = dstRange.elementAt(startIdx)..dstRange.elementAt(endIdx)
            return dstRes
        }
    }
}

fun MatchGroup?.asLong() = checkNotNull(this).value.toLong()
fun MatchGroup?.asString() = checkNotNull(this).value
