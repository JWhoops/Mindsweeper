package minesweeper

import java.util.Scanner
import kotlin.random.Random

fun main() {
    val scanner = Scanner(System.`in`)
    println("How many mines do you want on the field?")
    val mindNum = scanner.nextLine().filter { it.isDigit() }.toInt()
    val mindField = mutableListOf<CharArray>()
    initializeMindField(mindField, mindNum)
    printMindField(mindField)
}

fun initializeMindField(mindField: MutableList<CharArray>, mindNum:Int) {
    repeat(9) {
        val fieldRow = CharArray(9)
        for (i in fieldRow.indices)
            fieldRow[i] = '.'
        mindField.add(fieldRow)
    }
    repeat(mindNum) {
        var slot = ' '
        var row = -1
        var rIndex = -1
        while(slot != '.' && !slot.isDigit()) {
            val coordinate = getRandomSlot()
            row = coordinate[0]
            rIndex = coordinate[1]
            slot = mindField[row][rIndex]
        }
        mindField[row][rIndex] = 'X'
        labelNumberByCell(mindField, row, rIndex)
    }
}

fun labelNumberByCell(mindField: MutableList<CharArray>, row: Int, col: Int) {
    if(row-1 >= 0){
        updateCell(mindField, row-1, col)
        if(col - 1 >= 0) {
            updateCell(mindField, row-1, col-1)
        }
        if(col + 1 < 9) {
            updateCell(mindField, row-1 , col+1)
        }
    }
    if(row + 1 < mindField.size) {
        updateCell(mindField, row+1,col)
        if(col - 1 >= 0) {
            updateCell(mindField, row+1, col-1)
        }

        if(col + 1 < 9) {
            updateCell(mindField, row+1 , col+1)
        }
    }
    if(col - 1 >= 0) {
       updateCell(mindField, row, col-1)
    }
    if(col + 1 < 9) {
        updateCell(mindField, row , col+1)
    }
}

fun updateCell(mindField: MutableList<CharArray>, row: Int, col: Int) {
    var cell = mindField[row][col]
    if(cell.isDigit()) {
        val temp = cell.toInt()
        cell = (temp+1).toChar()
    } else if(!cell.isLetter()) {
        cell = '1'
    }
    mindField[row][col] = cell
}

fun getRandomSlot() : IntArray {
    val mindIndex = Random.nextInt(81)
    val row = mindIndex / 9
    val rIndex = mindIndex % 9
    return intArrayOf(row, rIndex)
}

fun printMindField(field: MutableList<CharArray>) {
    for(row in field) {
        for (slot in row)
            print(slot)
        println()
    }
}