package flashcards

class Flashcard(val term: String, val definition: String, var mistakes: Int) {

    override fun toString(): String {
        return term
    }

}
