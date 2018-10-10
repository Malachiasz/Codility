package dojo.cracker.code

import kotlin.math.roundToInt
import kotlin.math.sqrt

class CodeCracker {

    fun calculate2(): Long {
        for (i in 987654321L downTo 123456789L) {
            if (is9UniqueDigits(i)) {
                val square = Math.sqrt(reverseNumber(i).toDouble())
                if (square.roundToInt().toDouble() == square ) {
                    return i;
                }
            }
        }
        return -1
    }

    fun calculate(): Long {
        var x = 1L
        val sqrt = sqrt(987654321.0).toLong()
        val sqrtmin = sqrt(123456789.0).toLong()

        for (i in sqrtmin..sqrt) {
            val square = getSquareOfNumber(i)
            val y = reverseNumber(square)
            if (is9UniqueDigits(y)) {
                if (y > x) {
                    x = y
                }
            }
        }
        return x
    }

    fun getSquareOfNumber(number: Long): Long {
        return number * number
    }

    fun is9UniqueDigits(number: Long): Boolean {
        return number.toString().toList().distinct().size == 9 && number.toString().length == 9
    }

    fun reverseNumber(number: Long): Long {
        return number.toString().reversed().toLong()
    }
}