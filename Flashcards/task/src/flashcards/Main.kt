package flashcards

fun main() {
    val flashcardPack = mutableListOf<Flashcard>()
    println("Input the number of cards")
    val amount: Int
    while (true) {
        val input = readln()
        if (input.all { it.isDigit() }) {
            amount = input.toInt()
            break
        }
        else println("Invalid input")
    }
    for (i in 1 .. amount) {
        println("Card #$i")
        val term = readln()
        println("The definition for card #$i:")
        val definition = readln()
        flashcardPack.add(Flashcard(term, definition))
    }
    flashcardPack.forEach { flashcard ->
        println("Print the definition of \"${flashcard.term}\":")
        if (readln() == flashcard.definition)
            println("Correct!")
        else
            println("Wrong. The right answer is \"${flashcard.definition}\".")
    }
}

