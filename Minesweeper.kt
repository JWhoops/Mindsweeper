package minesweeper

import kotlin.random.Random

class Minesweeper(mindNum: Int) {
    private var mindNum = 0
    private val mindField = mutableListOf<CharArray>()
    private val minds = mutableListOf<String>()
    private val marks = mutableListOf<String>()

    init {
        this.mindNum = mindNum
    }

    fun stepOnMine(row: Int, col: Int): Boolean {
        return "${col - 1}${row - 1}" in minds
    }

    fun gameOver() {
        minds.forEach { coordinate ->
            val col = Character.getNumericValue(coordinate[0])
            val row = Character.getNumericValue(coordinate[1])
            mindField[row][col] = 'X'
        }
        printMindField()
        println("You stepped on a mine and failed!")
    }

    fun exploreCell(row: Int, col: Int) {
        val cell = mindField[row][col]
        if ("$col$row" !in minds && cell == '.' || cell == '*') {
            mindField[row][col] = '/'
            labelNumberForCell(row, col)
            if (mindField[row][col].isDigit()) return
            if (row - 1 >= 0) {
                exploreCell(row - 1, col)
                if (col - 1 >= 0)
                    exploreCell(row - 1, col - 1)
                if (col + 1 < 9)
                    exploreCell(row - 1, col + 1)
            }
            if (row + 1 < mindField.size) {
                exploreCell(row + 1, col)
                if (col - 1 >= 0)
                    exploreCell(row + 1, col - 1)
                if (col + 1 < 9)
                    exploreCell(row + 1, col + 1)
            }
            if (col - 1 >= 0)
                exploreCell(row, col - 1)
            if (col + 1 < 9)
                exploreCell(row, col + 1)
        }
    }

    fun checkWin(): Boolean {
        if (minds.size == marks.size && marks.containsAll(minds)) {
            return true
        }
        var unexploredCell = 0
        for (row in mindField) {
            for (cell in row) {
                if (cell == '.' || cell == '*')
                    unexploredCell += 1
            }
        }
        if (unexploredCell - minds.size == 0)
            return true
        return false
    }

    fun markCell(inputCol: Int, inputRow: Int) {
        when (mindField[inputRow][inputCol]) {
            '*' -> {
                mindField[inputRow][inputCol] = '.'
                marks.remove("${inputCol}${inputRow}")
            }
            '.' -> {
                mindField[inputRow][inputCol] = '*'
                marks.add("$inputCol$inputRow")
            }
            else -> {
                println("There is a number here!")
                return
            }
        }
        printMindField()
    }

    fun initializeMindField() {
        repeat(9) {
            val fieldRow = CharArray(9)
            for (i in fieldRow.indices)
                fieldRow[i] = '.'
            mindField.add(fieldRow)
        }
        while (minds.size != mindNum) {
            val coordinate = getRandomCell()
            val row = coordinate[0]
            val col = coordinate[1]
            if (minds.contains("$col$row")) continue
            minds.add("$col$row")
        }
    }

    private fun labelNumberForCell(row: Int, col: Int) {
        if (minds.contains("$col$row")) return
        val cell = mindField[row][col]
        var num = 0
        if (cell.isDigit()) num = cell.toInt()
        if (row - 1 >= 0) {
            num += updateCell(row - 1, col)
            if (col - 1 >= 0)
                num += updateCell(row - 1, col - 1)
            if (col + 1 < 9)
                num += updateCell(row - 1, col + 1)
        }
        if (row + 1 < mindField.size) {
            num += updateCell(row + 1, col)
            if (col - 1 >= 0)
                num += updateCell(row + 1, col - 1)
            if (col + 1 < 9)
                num += updateCell(row + 1, col + 1)
        }
        if (col - 1 >= 0)
            num += updateCell(row, col - 1)
        if (col + 1 < 9)
            num += updateCell(row, col + 1)
        if (num > 0) {
            mindField[row][col] = (num + 48).toChar()
        }
    }

    private fun updateCell(row: Int, col: Int): Int {
        return if (minds.contains("$col$row")) 1 else 0
    }

    private fun getRandomCell(): IntArray {
        val mindIndex = Random.nextInt(81)
        val row = mindIndex / 9
        val rIndex = mindIndex % 9
        return intArrayOf(row, rIndex)
    }

    fun printMindField() {
        println(" │123456789│")
        println("-│—--------│")
        for (i in mindField.indices) {
            print("${i + 1}│")
            for (slot in mindField[i])
                print(slot)
            print("│\n")
        }
        println("-│---------│")
    }
}