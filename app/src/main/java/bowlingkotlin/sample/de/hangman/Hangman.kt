package bowlingkotlin.sample.de.hangman

class Hangman(val word: String) {

    val intputCharArray : CharArray =  word.toCharArray()
    val guessedCharacters : MutableList<Char> = mutableListOf()
    var failures : Int = 0
    var isFinished = false;


    fun guess(character : Char) : String {
        if (isFinished) return "The game has ended."
        if (character in guessedCharacters) {
            return obfuscateWord()
        }
        guessedCharacters.add(character)
        if (failures > 6) {
            return "You got hung! The word was $word"
        } else {
            if (isSuccess()) {
                isFinished = true
                return "You found the word! $word"
            } else {
                failures++
            }
            return obfuscateWord()
        }
    }

    fun obfuscateWord(): String {
        var result = ""

        for (i in intputCharArray) {
            if (i in guessedCharacters) {
                result += i
            } else {
                result += "_"
            }
            result += " "
        }
        return result
    }

    fun isSuccess(): Boolean {
        for(i in intputCharArray) {
            if (!(i in guessedCharacters)) return false
        }
        return true
    }

}