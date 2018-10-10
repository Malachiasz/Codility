package dojo.cracker.code

import org.junit.Assert.*
import org.junit.Test

class CodeCrackerTest {

    @Test
    fun calculate() {
        val cracker = CodeCracker()
        var startTime = System.currentTimeMillis();
        System.out.println(cracker.calculate())
        System.out.println("Execution time:" + (System.currentTimeMillis() - startTime))


        startTime = System.currentTimeMillis();
        System.out.println(cracker.calculate2())
        System.out.println("Execution time:" + (System.currentTimeMillis() - startTime))
    }
}