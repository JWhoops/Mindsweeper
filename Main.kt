package minesweeper

import java.util.Scanner
import kotlin.random.Random

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? > ")
    val mindNum = scanner.nextLine().filter { it.isDigit() }.toInt()
    val mindField = mutableListOf<CharArray>()
    val minds = mutableListOf<String>()
    val marks = mutableListOf<String>()
    initializeMindField(mindField, minds, mindNum)
    printMindField(mindField)
    while (!checkWin(mindField, marks, minds)) {
       println("Set/delete mine marks (x and y coordinates):")
       print("> ")
       val inputCol = scanner.nextInt()
       val inputRow = scanner.nextInt()
       if(scanner.next() == "mine") {
           markCell(mindField, marks, inputCol - 1, inputRow - 1)
       } else {
           if(minds.contains("${inputCol-1}${inputRow-1}")) {
              gameOver(mindField, minds)
              return
           }
           freeCell(mindField, marks, minds, inputCol - 1, inputRow - 1)
           printMindField(mindField)
       }
    }
    println("Congratulations! You found all the mines!")
}

fun gameOver(mindField: MutableList<CharArray>, minds: MutableList<String>) {
    minds.forEach{
        coordinate ->
        val col = Character.getNumericValue(coordinate[0])
        val row = Character.getNumericValue(coordinate[1])
        mindField[row][col] = 'X'
    }
    printMindField(mindField)
    println("You stepped on a mine and failed!")
}

fun freeCell(mindField: MutableList<CharArray>, marks: MutableList<String>, minds: MutableList<String>, row: Int, col: Int) {
    val cell = mindField[row][col]
    if("$col$row" !in minds && cell == '.' || cell == '*') {
        mindField[row][col] = '/'
        labelNumberForCell(mindField, minds, row, col)
        if (mindField[row][col].isDigit()) return
        if (row - 1 >= 0) {
            freeCell(mindField, marks, minds, row - 1, col)
            if (col - 1 >= 0)
                freeCell(mindField, marks, minds, row - 1, col - 1)
            if (col + 1 < 9)
                freeCell(mindField, marks, minds, row - 1, col + 1)
        }
        if (row + 1 < mindField.size) {
            freeCell(mindField, marks, minds, row + 1, col)
            if (col - 1 >= 0)
                freeCell(mindField, marks, minds, row + 1, col - 1)
            if (col + 1 < 9)
                freeCell(mindField, marks, minds, row + 1, col + 1)
        }
        if (col - 1 >= 0)
            freeCell(mindField, marks, minds, row, col - 1)
        if (col + 1 < 9)
            freeCell(mindField, marks, minds, row, col + 1)
    }
}

fun checkWin(mindField: MutableList<CharArray>, minds: MutableList<String>, marks: MutableList<String>) :Boolean {
    if(minds.size == marks.size && marks.containsAll(minds)) {
        return true
    }
    var unexploredCell = 0
    for(row in mindField){
        for (cell in row) {
            if (cell == '.' || cell == '*')
                unexploredCell+=1
        }
    }
    if(unexploredCell - minds.size == 0)
        return true
    return false
}

fun markCell(mindField: MutableList<CharArray>, marks: MutableList<String>, inputCol: Int, inputRow: Int) {
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
   printMindField(mindField)
}

fun initializeMindField(mindField: MutableList<CharArray>, minds:MutableList<String>, mindNum:Int) {
    repeat(9) {
        val fieldRow = CharArray(9)
        for (i in fieldRow.indices)
            fieldRow[i] = '.'
        mindField.add(fieldRow)
    }
    while (minds.size != mindNum) {
        val coordinate = getRandomSlot()
        val row = coordinate[0]
        val col = coordinate[1]
        if(minds.contains("$col$row")) continue
        minds.add("$col$row")
    }
}

fun labelNumberForCell(mindField: MutableList<CharArray>, minds: MutableList<String>, row: Int, col: Int) {
    if(minds.contains("$col$row")) return
    val cell = mindField[row][col]
    var num = 0
    if(cell.isDigit()) num = cell.toInt()
    if(row-1 >= 0) {
        num+=updateCell(minds, row-1, col)
        if(col - 1 >= 0)
            num+=updateCell( minds, row-1, col-1)
        if(col + 1 < 9)
            num+=updateCell(minds, row-1 , col+1)
    }
    if(row + 1 < mindField.size) {
        num+=updateCell(minds, row+1,col)
        if(col - 1 >= 0)
            num+=updateCell(minds, row+1, col-1)
        if(col + 1 < 9)
            num+=updateCell( minds, row+1 , col+1)
    }
    if(col - 1 >= 0)
        num+=updateCell(minds, row, col-1)
    if(col + 1 < 9)
        num+=updateCell(minds, row , col+1)
    if (num > 0) {
        mindField[row][col] = (num + 48).toChar()
    }
}

fun updateCell(minds: MutableList<String>, row: Int, col: Int) : Int{
    return if(minds.contains("$col$row")) 1 else 0
}

fun getRandomSlot() : IntArray {
    val mindIndex = Random.nextInt(81)
    val row = mindIndex / 9
    val rIndex = mindIndex % 9
    return intArrayOf(row, rIndex)
}

fun printMindField(field: MutableList<CharArray>) {
    println(" │123456789│")
    println("-│—--------│")
    for(i in field.indices) {
        print("${i+1}│")
        for (slot in field[i])
            print(slot)
        print("│\n")
    }
    println("-│---------│")
}