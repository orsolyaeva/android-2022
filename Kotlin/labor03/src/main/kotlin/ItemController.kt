class ItemController(var itemService: ItemService) {
    constructor() : this(ItemService())

    fun quiz(number: Int) {
        val items = itemService.selectRandomItems(number)
        var countCorrect = 0

        items.forEach { item ->
            println("Q: " + item.question)
            item.answers.forEachIndexed() { index, answer ->
                println("${index + 1}: ${answer.second}")
            }

            print("Answer: ")
            var answer = readLine()!!.toInt()

            while(answer > item.answers.size || answer < 1) {
                print("\nInvalid answer! Try again!\nAnswer: ")
                answer = readLine()!!.toInt()
            }

            if (item.answers[answer - 1].first == item.correct) {
                println("Correct!\n")
                countCorrect += 1
            } else {
                println("Wrong!\nCorrect answer: ${item.correct}\n")
            }
        }

        println("Correct answers: $countCorrect")
        println("Total number of answers: ${items.size}")

        val percentage = (countCorrect.toDouble() / items.size.toDouble()) * 100
        println("Percentage: $percentage%")

        when {
            percentage > 100.0 -> println("Impossible, you are a genius!")
            percentage == 100.0 -> println("Perfect!")
            percentage >= 90.0 -> println("Excellent work!")
            percentage >= 80.0 -> println("Great!")
            percentage >= 70.0 -> println("Good job!")
            percentage >= 60.0 -> println("Not bad!")
            else -> println("You need to study more!")
        }

    }
}