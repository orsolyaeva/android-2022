import java.io.File

class ItemRepository {
    private val items =  mutableListOf<Item>()

    init {
        val file = File("questions.txt")
        val lines = file.readLines()
        var i = 0
        while (i < lines.size) {
            val question = lines[i++]
            val numberOfAnswers = lines[i].split(" ")[0].toInt()
            val correctAnswerNumber = lines[i++].split(" ")[1].toInt()
            val answers = mutableListOf<Pair<Int, String>>()
            for (j in 0 until numberOfAnswers) {
                answers.add(Pair(j + 1, lines[i++]))
            }
            items.add(Item(question, answers, correctAnswerNumber))
        }
    }

    fun randomItem(): Item {
        return items.random()
    }

    fun save(item: Item) {
        items.add(item)
    }

    fun size() : Int {
        return items.size
    }

    fun printItems() {
        for (item in items) {
            println(item)
        }
    }
}