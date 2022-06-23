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
        var term: String
        while (true) {
            term = readln()
            if (term.isBlank()) {
                println("Invalid entry")
            } else if (flashcardPack.any { it.term == term }) {
                println("The term \"$term\" already exists. Try again:")
            } else {
                break
            }
        }
        println("The definition for card #$i:")
        var definition: String
        while (true) {
            definition = readln()
            if (definition.isBlank()) {
                println("Invalid entry")
            } else if (flashcardPack.any { it.definition == definition }) {
                println("The definition \"$definition\" already exists. Try again:")
            } else {
                break
            }
        }
        flashcardPack.add(Flashcard(term, definition))
    }
    flashcardPack.forEach { flashcard ->
        println("Print the definition of \"${flashcard.term}\":")
        val answer = readln()
        if (answer == flashcard.definition) {
            println("Correct!")
        }
        else {
            print("Wrong. The right answer is \"${flashcard.definition}\"")
            val correctCard: Flashcard? = flashcardPack.firstOrNull { it.definition == answer }
            if (correctCard != null) {
                println(", but your definition is correct for \"${correctCard.term}\".")
            } else {
                println(".")
            }
        }
    }
}

