package flashcards

import java.io.File

val flashcardPack = mutableListOf<Flashcard>()
val logText = mutableListOf<String>()

fun main(args: Array<String>) {
    if (args.contains("-import")) {
        import(args[args.indexOf("-import") + 1])
    }
    while (true) {
        val menuText = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"
        println(menuText)
        logText.add(menuText)
        val input = readln()
        logText.add(input)
        when (input) {
            "add" -> add()
            "remove" -> remove()
            "import" -> import(fileNamePrompt())
            "export" -> export(fileNamePrompt())
            "ask" -> ask()
            "exit" -> {
                val exitMessage = "Bye bye!"
                println(exitMessage)
                logText.add(exitMessage)
                if (args.contains("-export")) {
                    export(args[args.indexOf("-export") + 1])
                }
                break
            }
            "log" -> log(fileNamePrompt())
            "hardest card" -> hardestCard()
            "reset stats" -> resetStats()
            else -> {
                val errorMessage = "Invalid entry"
                println(errorMessage)
                logText.add(errorMessage)
            }
        }
    }
}

fun add() {
    val cardMessage = "The card:"
    println(cardMessage)
    logText.add(cardMessage)
    val term = readln()
    logText.add(term)
    if (flashcardPack.any { it.term == term }) {
        val termErrorMessage = "The card \"$term\" already exists."
        println(termErrorMessage)
        logText.add(termErrorMessage)
        return
    } else if (term.isBlank()) {
        val termBlankMessage = "The card cannot be blank"
        println(termBlankMessage)
        logText.add(termBlankMessage)
        return
    }
    val definitionMessage = "The definition of the card:"
    println(definitionMessage)
    logText.add(definitionMessage)
    val definition = readln()
    logText.add(definition)
    if (flashcardPack.any { it.definition == definition }) {
        val definitionErrorMessage = "The definition \"$definition\" already exists:"
        println(definitionErrorMessage)
        logText.add(definitionErrorMessage)
        return
    } else if (definition.isBlank()) {
        val definitionBlankMessage = "The definition cannot be blank"
        println(definitionBlankMessage)
        logText.add(definitionBlankMessage)
        return
    }
    flashcardPack.add(Flashcard(term, definition, 0))
    val pairAddedMessage = "The pair (\"$term\":\"$definition\") has been added."
    println(pairAddedMessage)
    logText.add(pairAddedMessage)
}

fun remove() {
    val cardMessage = "Which card?"
    println(cardMessage)
    logText.add(cardMessage)
    val cardToRemove = readln()
    logText.add(cardToRemove)
    if (flashcardPack.removeIf { it.term == cardToRemove } || flashcardPack.removeIf { it.definition == cardToRemove }) {
        val cardRemovedMessage = "The card has been removed"
        println(cardRemovedMessage)
        logText.add(cardRemovedMessage)
    } else {
        val cardRemovedError = "Can't remove \"$cardToRemove\": there is no such card."
        println(cardRemovedError)
        logText.add(cardRemovedError)
    }
}

fun fileNamePrompt() : String {
    val filePrompt = "File name:"
    println(filePrompt)
    logText.add(filePrompt)
    val input = readln()
    logText.add(input)
    return input
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
        val cardsLoadedMessage = "$count cards have been loaded."
        println(cardsLoadedMessage)
        logText.add(cardsLoadedMessage)
    } catch (e: java.lang.Exception) {
        val fileNotFoundMessage = "File not found."
        println(fileNotFoundMessage)
        logText.add(fileNotFoundMessage)
    }
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
        val cardsSavedMessage = "${flashcardPack.size} cards have been saved."
        println(cardsSavedMessage)
        logText.add(cardsSavedMessage)
    } catch (e: java.lang.Exception) {
        val writeErrorMessage = "Write Error"
        println(writeErrorMessage)
        logText.add(writeErrorMessage)
    }
}

fun ask() {
    if (flashcardPack.isEmpty()) {
        val noFlashcardsErrorMessage = "There are no flashcards available"
        println(noFlashcardsErrorMessage)
        logText.add(noFlashcardsErrorMessage)
        return
    }
    val askAmountPrompt = "How many times to ask?"
    println(askAmountPrompt)
    logText.add(askAmountPrompt)
    val input = readln()
    logText.add(input)
    val timesToAsk = input.toIntOrNull()
    if (timesToAsk == null) {
        val invalidEntryErrorMessage = "Invalid Entry"
        println(invalidEntryErrorMessage)
        logText.add(invalidEntryErrorMessage)
        return
    }
    for (i in 0 until timesToAsk) {
        val randomCard = flashcardPack.random()
        val askForDefinitionPrompt = "Print the definition of \"${randomCard.term}\":"
        println(askForDefinitionPrompt)
        logText.add(askForDefinitionPrompt)
        val answer = readln()
        logText.add(answer)
        if (answer == randomCard.definition) {
            val correctAnswerMessage = "Correct!"
            println(correctAnswerMessage)
            logText.add(correctAnswerMessage)
        }
        else {
            var wrongAnswerMessage = "Wrong. The right answer is \"${randomCard.definition}\""
            val correctCard: Flashcard? = flashcardPack.firstOrNull { it.definition == answer }
            wrongAnswerMessage += if (correctCard != null) {
                ", but your definition is correct for \"${correctCard.term}\"."
            } else {
                "."
            }
            println(wrongAnswerMessage)
            logText.add(wrongAnswerMessage)
            randomCard.mistakes++
        }
    }
}

fun log(fileName: String) {
    try {
        val logFile = File(fileName)
        val logSavedMessage = "The log has been saved."
        println(logSavedMessage)
        logText.add(logSavedMessage)
        logText.forEach { logFile.appendText(it + "\n") }
    } catch (e: java.lang.Exception) {
        val writeErrorMessage = "Write Error"
        println(writeErrorMessage)
        logText.add(writeErrorMessage)
    }
}

fun hardestCard() {
    var mostMistakes = 0
    flashcardPack.forEach {
        if (it.mistakes > mostMistakes) {
            mostMistakes = it.mistakes
        }
    }
    if (mostMistakes == 0) {
        val noErrorsMessage = "There are no cards with errors"
        println(noErrorsMessage)
        logText.add(noErrorsMessage)
        return
    }
    val hardestCards = flashcardPack.filter { it.mistakes == mostMistakes }
    val hardestCardsMessage = ("The hardest card"
            + (if (hardestCards.size > 1) "s are " else " is ")
            + hardestCards.joinToString { "\"${it.term}\"" }
            + "."
            + " You have $mostMistakes error${if (mostMistakes > 1) "s" else ""} answering "
            + if (hardestCards.size > 1) "them." else "it.")
    println(hardestCardsMessage)
    logText.add(hardestCardsMessage)
}

fun resetStats() {
    flashcardPack.forEach { it.mistakes = 0 }
    val resetMessage = "Card statistics have been reset."
    println(resetMessage)
    logText.add(resetMessage)
}

