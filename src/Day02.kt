sealed class SubmarineInstruction {
    // TODO: understand what default constructor of sealed classes mean
    data class Forward(val numberOfUnits: Int) : SubmarineInstruction()
    data class Down(val numberOfUnits: Int) : SubmarineInstruction()
    data class Up(val numberOfUnits: Int) : SubmarineInstruction()

    companion object ParsedInstruction {
        fun parseInstruction(rawInstruction: String): SubmarineInstruction {
            rawInstruction.split(" ")
                .let { Pair(it[0], it[1].toInt()) }
                .let {
                    return when (it.first) {
                        "forward" -> Forward(it.second)
                        "down" -> Down(it.second)
                        "up" -> Up(it.second)
                        else -> throw IllegalArgumentException()
                    }
                }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it -> SubmarineInstruction.parseInstruction(it) }
            // Pair - first is horizontalPosition, second is depthPosition
            .fold(Pair(0, 0)) { positionPair, instruction ->
                when (instruction) {
                    is SubmarineInstruction.Forward -> Pair(
                        positionPair.first + instruction.numberOfUnits,
                        positionPair.second
                    )

                    // reverse operation, cuz "sub"marine
                    is SubmarineInstruction.Down -> Pair(
                        positionPair.first,
                        positionPair.second + instruction.numberOfUnits
                    )

                    is SubmarineInstruction.Up -> Pair(
                        positionPair.first,
                        positionPair.second - instruction.numberOfUnits
                    )
                }
            }
            // of course there's a better way to multiply the two elements of a pair ;)
            .toList()
            .fold(1) { productSoFar, n ->
                productSoFar * n
            }
    }

    fun part2(input: List<String>): Int {
        return -input
            .map { it -> SubmarineInstruction.parseInstruction((it)) }
            // Triple - first is horizontalPosition, second is depthPosition, third is aim
            .fold(Triple(0, 0, 0)) { submarineTriple, instruction ->
                when (instruction) {
                    is SubmarineInstruction.Down -> Triple(
                        submarineTriple.first,
                        submarineTriple.second,
                        submarineTriple.third - instruction.numberOfUnits
                    )

                    is SubmarineInstruction.Up -> Triple(
                        submarineTriple.first,
                        submarineTriple.second,
                        submarineTriple.third + instruction.numberOfUnits
                    )

                    is SubmarineInstruction.Forward -> Triple(
                        submarineTriple.first + instruction.numberOfUnits,
                        submarineTriple.second + submarineTriple.third * instruction.numberOfUnits,
                        submarineTriple.third
                    )
                }
            }
            .let {
                it.first * it.second
            }
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
