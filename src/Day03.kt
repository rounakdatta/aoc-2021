import kotlin.math.pow

fun binaryToDecimal(input: List<Char>): Int {
    // pair of index, result
    return input.foldRight(Pair(-1, 0)) { bit, pairOfIndexAndResultSoFar ->
        Pair(
            pairOfIndexAndResultSoFar.first + 1,
            (pairOfIndexAndResultSoFar.second + bit.digitToInt() * 2.0.pow((pairOfIndexAndResultSoFar.first + 1))).toInt()
        )
    }
        .second
}

fun getMostCommonElement(input: List<String>, position: Int): Char {
    return input
        .fold(0) { accumulator, binary ->
            accumulator + if (binary[position] == '1') 1 else 0
        }
        .let { if (it >= (input.size - it)) '1' else '0' }
}

fun getLeastCommonElement(input: List<String>, position: Int): Char {
    return input
        .fold(0) { accumulator, binary ->
            accumulator + if (binary[position] == '1') 1 else 0
        }
        .let { if (it >= (input.size - it)) '0' else '1' }
}

fun applyListReduction(input: List<String>, position: Int, reductionElement: Char): List<String> {
    return input
        .filter { it[position] == reductionElement }
}

fun computeOxygenGeneratorRating(input: List<String>, position: Int): Int {
    // 11010, 11111, 010101, 10010 (check at 0 position from left)
    // 11010, 11111, 10010 (check at 1 position from left)
    // 11010, 11111 (check at 2 position from left)
    // 11111 (answer)

    return when (input.size) {
        1 -> binaryToDecimal(input.first().toList())
        else -> {
            computeOxygenGeneratorRating(
                applyListReduction(input, position, getMostCommonElement(input, position)),
                position + 1
            )
        }
    }
}

fun computeCO2ScrubberRating(input: List<String>, position: Int): Int {
    return when (input.size) {
        1 -> binaryToDecimal(input.first().toList())
        else -> {
            computeCO2ScrubberRating(
                applyListReduction(input, position, getLeastCommonElement(input, position)),
                position + 1
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        // List of lots of elements like [0, 0, 1, 1, 0]
        // [1s, 1s, 1s, 1s, 1s] -> count majority [10110]
        // Pair(gamma, epsilon)

        return input
            .map { it -> it.toList() }
            .fold(IntArray(input.first().length) { 0 }) { collectingList, binaryNumberAsList ->
                // TODO: this forEachIndexed mutates values directly, can be done better
                binaryNumberAsList.forEachIndexed { index, i ->
                    collectingList[index] += if (i == '1') {
                        1
                    } else {
                        0
                    }
                }
                collectingList
            }
            // now I'll construct the gamma value
            .fold("") { collectingString, sumOf1sOnIndex ->
                collectingString + if (sumOf1sOnIndex > (input.size - sumOf1sOnIndex)) {
                    "1"
                } else {
                    "0"
                }
            }
            .toList()
            .let {
                Pair(binaryToDecimal(List(input.first().length) { '1' }), binaryToDecimal(it))
            }
            .let { (it.first - it.second) * it.second }
    }

    fun part2(input: List<String>): Int {
        return input
            .let { it -> computeOxygenGeneratorRating(it, 0) * computeCO2ScrubberRating(it, 0) }
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
