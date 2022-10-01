fun parseLine(input: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    return input.split(" -> ")
        .map { it -> it.split(",") }
        .map { it -> Pair(it.first().toInt(), it.last().toInt()) }
        .let {
            Pair(it.first(), it.last())
        }
}

fun drawStraightLineOnChart(chart: MutableList<MutableList<Int>>, straightLine: Pair<Pair<Int, Int>, Pair<Int, Int>>): MutableList<MutableList<Int>> {
    // 7,0 -> 7,4
    // 7,0 7,1 7,2 7,3, 7,4

    return when(Pair(straightLine.first.first == straightLine.second.first, straightLine.first.second == straightLine.second.second)) {
        // when the line is vertical i.e. x is fixed
        Pair(true, false) -> {
            (straightLine.first.second until straightLine.second.second + 1)
                .forEach { it -> chart[straightLine.first.first][it] += 1 }

            (straightLine.second.second until straightLine.first.second + 1)
                .forEach { it -> chart[straightLine.first.first][it] += 1 }

            chart
        }
        // when the line is horizontal
        else -> {
            (straightLine.first.first until straightLine.second.first + 1)
                .forEach { it -> chart[it][straightLine.first.second] += 1 }

            (straightLine.second.first until straightLine.first.first + 1)
                .forEach { it -> chart[it][straightLine.first.second] += 1 }

            chart
        }
    }
}

fun drawDiagonalLineOnChart(chart: MutableList<MutableList<Int>>, diagonalLine: Pair<Pair<Int, Int>, Pair<Int, Int>>): MutableList<MutableList<Int>> {
    // (4,8) (5,7) (6,6) (7,5) (8,4), (9,3)

    // directed towards fourth quadrant e.g. 4,8 -> 9,3
    return if (diagonalLine.first.first < diagonalLine.second.first && diagonalLine.first.second > diagonalLine.second.second) {
        (diagonalLine.first.first until diagonalLine.second.first + 1).map { xCoordinate ->
            chart[xCoordinate][diagonalLine.first.second - (xCoordinate - diagonalLine.first.first)] += 1
        }
            .let {
                chart
            }

        // directed towards the third quadrant e.g. 4,8 -> 2,6
    } else if (diagonalLine.second.first < diagonalLine.first.first && diagonalLine.first.second > diagonalLine.second.second) {
        (diagonalLine.second.first until diagonalLine.first.first + 1).map { xCoordinate ->
            chart[xCoordinate][diagonalLine.second.second + (xCoordinate - diagonalLine.second.first)] += 1
        }
            .let {
                chart
            }

        // directed towards the second quadrant e.g. 4,8 -> 2,10
    } else if (diagonalLine.second.first < diagonalLine.first.first && diagonalLine.second.second > diagonalLine.first.second) {
        (diagonalLine.second.first until diagonalLine.first.first + 1).map { xCoordinate ->
            chart[xCoordinate][diagonalLine.second.second - (xCoordinate - diagonalLine.second.first)] += 1
        }
            .let {
                chart
            }

        // directed towards the first quadrant i.e. 4,8 -> 7,11
    } else if (diagonalLine.first.first < diagonalLine.second.first && diagonalLine.second.second > diagonalLine.first.second) {
        (diagonalLine.first.first until diagonalLine.second.first + 1).map { xCoordinate ->
            chart[xCoordinate][diagonalLine.first.second + (xCoordinate - diagonalLine.first.first)] += 1
        }
            .let {
                chart
            }

    } else {
        // unlikely case, never to be reached
        chart
    }
}

fun countOverlaps(chart: MutableList<MutableList<Int>>): Int {
    return chart
        .map { it -> it.filter { itx -> itx > 1 }.size }
        .sum()
}

fun checkIfStraightOrDiagonal(pointPair: Pair<Pair<Int, Int>, Pair<Int, Int>>): String {
    return if (pointPair.first.first == pointPair.second.first || pointPair.first.second == pointPair.second.second) {
        "straight"
    } else if ((pointPair.first.first == pointPair.first.second && pointPair.second.first == pointPair.second.second) || ((pointPair.first.first - pointPair.first.second) % 2 == 0 && (pointPair.second.first - pointPair.second.second) % 2 == 0)) {
        "diagonal"
    } else {
        "invalid"
    }
}

fun main() {
    val maxBoardSize = 1000

    fun part1(input: List<String>): Int {
        return input.map { parseLine(it) }
                // filter out those lying in a straight line
            .filter { it.first.first == it.second.first || it.first.second == it.second.second }
            .fold(MutableList(maxBoardSize) {MutableList(maxBoardSize) {0}}) { chart, straightLine ->
                chart.apply { drawStraightLineOnChart(chart, straightLine) }
            }
            .let { countOverlaps(it) }
    }

    fun part2(input: List<String>): Int {
        return input.map { parseLine(it) }
            // .filter { it.first.first == it.second.first || it.first.second == it.second.second || (it.first.first == it.first.second && it.second.first == it.second.second) }
            .map { it -> Pair(it, checkIfStraightOrDiagonal(it)) }
            .filter { it -> it.second == "straight" || it.second == "diagonal" }
            .fold(MutableList(maxBoardSize) {MutableList(maxBoardSize) {0}}) { chart, typeOfLine ->
                chart.apply { if (typeOfLine.second == "straight") { drawStraightLineOnChart(chart, typeOfLine.first) } else { drawDiagonalLineOnChart(chart, typeOfLine.first) } }
            }
//            .also {
//                it.forEach { itx -> println(itx) }
//            }
            .let { countOverlaps(it) }
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
