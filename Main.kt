package minesweeper
import java.util.Scanner
import kotlin.random.Random.Default.nextInt

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val n = getNum("How many mines do you want on the field? ", false)
    var minesweeper = Minesweeper(n)
    minesweeper.printGuessArray()

    while (minesweeper.minesToFound > 0) {
        print ("Set/delete mines marks (x and y coordinates): ")
        val x = scanner.nextInt()
        val y = scanner.nextInt()
        minesweeper.setDeleteMineMark(x, y)
        minesweeper.printGuessArray()
//        minesweeper.printArray()
    }
    println("Congratulations! You found all the mines!")
}

class Minesweeper {
    var array = Array(9) { CharArray(9) }
    var guessArray = Array(9) { CharArray(9) }
    var minesToFound = Int.MAX_VALUE
    constructor(n: Int) {
        minesToFound = n
        var mineArray = IntArray(n)
        for (i in 0..n-1) {
            while (true) {
                val newInt = nextInt(0, 80)
                if (newInt !in mineArray) {
                    mineArray.set(i, newInt)
                    break
                }
            }
        }
        var count = 0

        // construct array
        for (i in 0..8) {
            for (j in 0..8) {
                if (count in mineArray) {
                    array[i][j] = 'X'
                } else {
                    array[i][j] = '.'
                }
                count++
            }
        }

        // construct guessArray
        for (i in 0..8) {
            for (j in 0..8) {
                val minesAround = countAround(array, i, j)
                if (minesAround > 0) {
                    guessArray[i][j] = (minesAround.toString().first())
                } else {
                    guessArray[i][j] = '.'
                }

            }
        }

    }

    fun printGuessArray() {
        for (i in 0..11) {
            for (j in 0..11) {
                if (i == 0 && j == 0) {
                    print(" ")
                } else if (j == 1 || j == 11) {
                    print("|")
                } else if (i == 1 || i == 11) {
                    print("-")
                } else if (i in 2..10 && j == 0) {
                    print(i-1)
                } else if (j in 2..10 && i == 0) {
                    print(j-1)
                } else {
                    print(guessArray[j-2][i-2])
                }
            }
            println()
        }
    }

    fun printArray() {
        for (i in 0..11) {
            for (j in 0..11) {
                if (i == 0 && j == 0) {
                    print(" ")
                } else if (j == 1 || j == 11) {
                    print("|")
                } else if (i == 1 || i == 11) {
                    print("-")
                } else if (i in 2..10 && j == 0) {
                    print(i-1)
                } else if (j in 2..10 && i == 0) {
                    print(j-1)
                } else {
                    print(array[j-2][i-2])
                }
            }
            println()
        }
    }

    fun setDeleteMineMark (x: Int, y: Int) {
        if (array[x-1][y-1].isDigit()) {
            println("There is a number here!")
        } else if (guessArray[x-1][y-1] == '*') {
            guessArray[x-1][y-1] = '.'
            if (array[x-1][y-1] == 'X')
            {
                ++minesToFound
            }
        } else if (guessArray[x-1][y-1] == '.') {
            guessArray[x-1][y-1] = '*'
            if (array[x-1][y-1] == 'X')
            {
                --minesToFound
            }
        }
    }

    fun deleteMineMark (x: Int, y: Int): Boolean {
        return if (guessArray[x-1][y-1] == '*') {
            guessArray[x-1][y-1] == '.'
            true
        } else {
            false
        }
    }

}

fun countAround(array: Array<CharArray>, i: Int, j: Int): Int {
    var mines = 0
    if (array[i][j] == 'X') {
        return 0
    }
    for (x in i-1..i+1) {
        for (y in j-1..j+1) {
            if (!(x==i && y==j) && mustCount(x, y)) {
                if (array[x][y] == 'X') {
                    mines++
                }
            }
        }
    }
    return mines
}

fun mustCount(x: Int, y: Int): Boolean {
    return x in 0..8 && y in 0..8
}

fun getNum(text: String, defaultMessage: Boolean = true): Int {
    val strErrorNum = " was not a number, please try again: "
    var num = text
    var default = defaultMessage

    do {
        print(if (default) num + strErrorNum else num)
        if (!default) default = true
        num = readLine()!!
    } while (!isNumber(num))

    return num.toInt()
}





fun isNumber(number: String) = number.toIntOrNull() != null
