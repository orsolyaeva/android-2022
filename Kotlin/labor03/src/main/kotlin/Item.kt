data class Item(val question : String, val answers: List<Pair<Int, String>>, val correct: Int)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (question != other.question) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question.hashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + correct
        return result
    }
}