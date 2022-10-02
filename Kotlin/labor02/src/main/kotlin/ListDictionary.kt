import java.io.File

object ListDictionary : IDictionary {
    val words = mutableListOf<String>()

    init {
        File(IDictionary.FILE_NAME).forEachLine { add(it) }
    }

    override fun add(word: String): Boolean {
        if (words.contains(word)) return false
        words.add(word)
        return true
    }

    override fun find(word: String): Boolean {
        return words.contains(word)
    }

    override fun size(): Int {
        return words.size
    }

}