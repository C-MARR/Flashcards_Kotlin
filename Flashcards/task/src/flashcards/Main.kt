package flashcards

val flashcardPack = mutableListOf<Flashcard>()

fun main() {
    while (true) {
        println("Input the action (add, remove, import, export, ask, exit):")
        when (readln()) {
            "add" -> add()
            "remove" -> remove()
            "import" -> import()
            "export" -> export()
            "ask" -> ask()
            "exit" -> {
                println("Bye bye!")
                break
            }
            else -> println("Invalid entry")
        }
    }
}

fun add() {
    println("The card:")
    val term = readln()
    if (flashcardPack.any { it.term == term }) {
        println("The term \"$term\" already exists.")
        return
    } else if (term.isBlank()) {
        println("The card cannot be blank")
        return
    }
    println("The definition for card:")
    val definition = readln()
    if (flashcardPack.any { it.definition == definition }) {
        println("The definition \"$definition\" already exists:")
        return
    } else if (definition.isBlank()) {
        println("The definition cannot be blank")
        return
    }
    flashcardPack.add(Flashcard(term, definition))
    println("The pair (\"$term\":\"$definition\") has been added.")
}

fun remove() {
    println("Which card?")
    val cardToRemove = readln()
    if (flashcardPack.removeIf { it.term == cardToRemove } || flashcardPack.removeIf { it.definition == cardToRemove }) {
        println("The card has been removed")
    } else {
        println("Can't remove \"$cardToRemove\": there is no such card.")
    }
}

fun import() {
    TODO("Not yet implemented")
}

fun export() {
    TODO("Not yet implemented")
}

fun ask() {
    if (flashcardPack.isEmpty()) {
        println("There are no flashcards available")
        return
    }
    println("How many times to ask?")
    val timesToAsk = readln().toIntOrNull()
    if (timesToAsk == null) {
        println("Invalid Entry")
        return
    }
    for (i in 0 until timesToAsk) {
        val randomCard = flashcardPack.random()
        println("Print the definition of \"${randomCard.term}\":")
        val answer = readln()
        if (answer == randomCard.definition) {
            println("Correct!")
        }
        else {
            print("Wrong. The right answer is \"${randomCard.definition}\"")
            val correctCard: Flashcard? = flashcardPack.firstOrNull { it.definition == answer }
            if (correctCard != null) {
                println(", but your definition is correct for \"${correctCard.term}\".")
            } else {
                println(".")
            }
        }
    }
}

