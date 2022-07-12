package flashcards

import java.io.File

val flashcardPack = mutableListOf<Flashcard>()

fun main(args: Array<String>) {
    if (args.contains("-import")) {
        import(args[args.indexOf("-import") + 1])
    }
    while (true) {
        println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        when (readln()) {
            "add" -> add()
            "remove" -> remove()
            "import" -> import()
            "export" -> export()
            "ask" -> ask()
            "exit" -> {
                println("Bye bye!")
                if (args.contains("-export")) {
                    export(args[args.indexOf("-export") + 1])
                }
                break
            }
            "log" -> log()
            "hardest card" -> hardestCard()
            "reset stats" -> resetStats()
            else -> println("Invalid entry")
        }
    }
}

fun resetStats() {
    flashcardPack.forEach { it.mistakes = 0 }
    println("Card statistics have been reset.")
}

fun hardestCard() {
    var mostMistakes = 0
    flashcardPack.forEach {
        if (it.mistakes > mostMistakes) {
            mostMistakes = it.mistakes
        }
    }
    if (mostMistakes == 0) {
        println("There are no cards with errors")
        return
    }
    val hardestCards = flashcardPack.filter { it.mistakes == mostMistakes }
    println("The hardest card"
            + (if (hardestCards.size > 1) "s are " else " is ")
            + hardestCards.joinToString { "\"${it.term}\"" }
            + "."
            + " You have $mostMistakes error${if (mostMistakes > 1) "s" else ""} answering "
            + if (hardestCards.size > 1) "them." else "it."
    )
}

fun log() {
    TODO("Not yet implemented")
}

fun add() {
    println("The card:")
    val term = readln()
    if (flashcardPack.any { it.term == term }) {
        println("The card \"$term\" already exists.")
        return
    } else if (term.isBlank()) {
        println("The card cannot be blank")
        return
    }
    println("The definition of the card:")
    val definition = readln()
    if (flashcardPack.any { it.definition == definition }) {
        println("The definition \"$definition\" already exists:")
        return
    } else if (definition.isBlank()) {
        println("The definition cannot be blank")
        return
    }
    flashcardPack.add(Flashcard(term, definition, 0))
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
    println("File name:")
    val fileName = readln()
    import(fileName)
}

fun import(fileName: String) {
    var count = 0
    try {
        val saveFile = File(fileName)
        saveFile.readLines().forEach { fullLine ->
            val line = fullLine.split(" :: ")
            if (line.size == 3) {
                flashcardPack.removeIf { it.term == line[0] }
                flashcardPack.add(Flashcard(line[0], line[1], line[2].toInt()))
                count++
            }
        }
        println("$count cards have been loaded.")
    } catch (e: java.lang.Exception) {
        println("File not found.")
    }
}

fun export() {
    println("File name:")
    val fileName = readln()
    export(fileName)
}

fun export(fileName: String) {
    if (flashcardPack.isEmpty()) {
        return
    }
    try {
        val saveFile = File(fileName)
        saveFile.writeText("")
        flashcardPack.forEach {
            saveFile.appendText("${it.term} :: ${it.definition} :: ${it.mistakes}\n")
        }
        println("${flashcardPack.size} cards have been saved.")
    } catch (e: java.lang.Exception) {
        println("Write Error")
    }
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
            randomCard.mistakes++
        }
    }
}

