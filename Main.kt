package minesweeper

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? > ")
    val mindNum = scanner.nextLine().filter { it.isDigit() }.toInt()
    val ms = Minesweeper(mindNum)
    ms.initializeMindField()
    ms.printMindField()
    while (!ms.checkWin()) {
        println("Set/delete mine marks (x and y coordinates):")
        print("> ")
        val inputCol = scanner.nextInt()
        val inputRow = scanner.nextInt()
        if (scanner.next() == "mine") {
            ms.markCell(inputCol - 1, inputRow - 1)
        } else {
            if (ms.stepOnMine(inputRow - 1, inputCol - 1)) {
                ms.gameOver()
                return
            }
            ms.exploreCell(inputRow - 1, inputCol - 1)
            ms.printMindField()
        }
    }
    println("Congratulations! You found all the mines!")
}
