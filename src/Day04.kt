class Board(input: MutableList<MutableList<Pair<String, Boolean>>>, isCompleted: Boolean) {
    private val actualBoard = input
    var completed: Boolean = isCompleted

    fun applyNumber(numberToPlayWith: String): Board {
        return Board(this.actualBoard.map {
            it.map { itx ->
                when (itx.first) {
                    numberToPlayWith -> Pair(itx.first, true)
                    else -> itx
                }
            }
                .toMutableList()
        }
            .toMutableList(),
            this.completed
        )
    }

    fun markBoardCompletion(): Boolean {
        // check columns
        // :( couldn't do this in functional style
        for (i in 0 until this.actualBoard.size) {
            val currentColumn = mutableListOf<Pair<String, Boolean>>()
            for (j in 0 until this.actualBoard.size) {
                currentColumn.add(this.actualBoard[j][i])
            }
            this.completed = currentColumn
                .filter { !it.second }.isEmpty()
            if (this.completed) {
                return true
            }
        }

        // check for rows
        this.completed = this.actualBoard
            .map { it -> it.filter { itx -> !itx.second }.size }
            .filter { it -> it == 0 }
            .isNotEmpty()

        return this.completed
    }

    fun calculateSumOfUnmarkedNumbers(): Int {
        return this.actualBoard
            .map { it ->
                it.filter { itx -> !itx.second }.sumOf { itx -> itx.first.toInt() }
            }
            .sumOf { it }
    }
}

fun deserializeBoard(input: List<String>): MutableList<MutableList<Pair<String, Boolean>>> {
    // remember to throw out the first blank row
    return input.fold(mutableListOf<MutableList<Pair<String, Boolean>>>()) { rows, singleRow ->
        rows.apply {
            this.add(
                singleRow.trim().split(" ", "  ")
                    // sometimes it catches the extra spaces as a number, we gotta throw that
                    .filter { it != "" }
                    .map { it -> Pair(it, false) }
                    .toMutableList()
            )
        }
    }
        .filter {
            it.size != 0
        }
        .toMutableList()
}

fun constructBoards(input: List<String>): MutableList<Board> {
    return input.chunked(6)
        .fold(mutableListOf<Board>()) { listOfBoards, serializedBoard ->
            listOfBoards.apply {
                this.add(Board(deserializeBoard(serializedBoard), false))
            }
        }
}

fun getChosenNumbers(input: String): List<String> {
    return input.split(",")
}

fun computeFirstScore(numberJustCalled: Int, boards: List<Board>): Int {
    return boards.filter { it.completed }.first().calculateSumOfUnmarkedNumbers() * numberJustCalled
}

fun letAllBoardsPlayBingo(
    chosenNumbers: List<String>,
    boards: MutableList<Board>,
    winnerScore: Int
): Pair<MutableList<Board>, Int> {
    return when (chosenNumbers.size) {
        0 -> Pair(boards, winnerScore)
        else -> {
            // play with the first element
            val numberToPlayWith = chosenNumbers.first()
            val appliedBoards = boards
                .map { it -> it.applyNumber(numberToPlayWith).also { it.markBoardCompletion() } }
            letAllBoardsPlayBingo(
                chosenNumbers.subList(1, chosenNumbers.size), appliedBoards.toMutableList(),
                if (winnerScore == -1 && appliedBoards.filter { it.completed }.isNotEmpty()) {
                    computeFirstScore(numberToPlayWith.toInt(), appliedBoards)
                } else {
                    winnerScore
                }
            )
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Pair(getChosenNumbers(input[0]), constructBoards(input.subList(1, input.size)))
            .let { it -> letAllBoardsPlayBingo(it.first, it.second, -1) }.second
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
