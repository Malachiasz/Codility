package bowlingkotlin.sample.de.hangman

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HangmanTest {
    lateinit var hangman: Hangman

    @Before
    fun setUp() {
        hangman = Hangman("testword")
    }

    @Test
    fun obfuscateWord() {
        assertEquals("_ _ _ _ _ _ _ _ ", hangman.obfuscateWord())
        assertEquals("_ _ _ _ _ _ _ _ ", hangman.guess('x'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('t'))
    }

    @Test
    fun deadAfter7falseAttempts() {
        assertEquals("_ _ _ _ _ _ _ _ ", hangman.guess('x'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('t'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('q'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('u'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('i'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('l'))
        assertEquals("t _ _ t _ _ _ _ ", hangman.guess('p'))
        assertEquals("You got hung! The word was testword", hangman.guess('g'))
    }

    @Test
    fun foundWord() {
        hangman.guess('t')
        hangman.guess('e')
        hangman.guess('s')
        hangman.guess('w')
        hangman.guess('o')
        hangman.guess('r')
        assertEquals("You found the word! testword", hangman.guess('d'))
    }

    @Test
    fun gameHasFinished() {
        hangman.guess('t')
        hangman.guess('e')
        hangman.guess('s')
        hangman.guess('w')
        hangman.guess('o')
        hangman.guess('r')
        hangman.guess('d')
        assertEquals("The game has ended.", hangman.guess('d'))
    }
}