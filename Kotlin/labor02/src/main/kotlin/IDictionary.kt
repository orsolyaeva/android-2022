interface IDictionary {
    companion object {
        const val FILE_NAME = "dictionary.txt"
    }

    fun add(word: String) : Boolean
    fun find(word: String) : Boolean
    fun size() : Int
}