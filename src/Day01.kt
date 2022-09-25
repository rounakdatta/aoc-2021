fun main() {
    fun part1(input: List<String>): Int {
        return input.zipWithNext { firstElement, secondElement ->
            secondElement.toInt() > firstElement.toInt()
        }
                .filter { it }
                .size

        // note that we could have used .windowed as well
        // watch me make use of .windowed in the next part
    }

    fun part2(input: List<String>): Int {
        return input.windowed(3).map { it ->
            it.map { _it ->
                _it.toInt()
            }
                    .sum()
        }
                .zipWithNext { first, second ->
                    second > first
                }
                .filter { it }
                .size

    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
