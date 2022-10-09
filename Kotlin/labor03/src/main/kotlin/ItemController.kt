class ItemController(var itemService: ItemService) {
    constructor() : this(ItemService())

    fun quiz(number: Int) {
        val items = itemService.selectRandomItems(number)
        var countCorrect = 0

        // print each question and check the answer from the user
        items.forEach { item ->
            println("Q: " + item.question)
            item.answers.forEachIndexed() { index, answer ->
                println("${index + 1}: ${answer.second}")
            }

            print("Answer: ")
            var answer = readLine()!!.toInt()

            // while the answer is not valid, ask againS
            while(answer > item.answers.size || answer < 1) {
                print("\nInvalid answer! Try again!\nAnswer: ")
                answer = readLine()!!.toInt()
            }

            // check if the answer is correct
            if (item.answers[answer - 1].first == item.correct) {
                println("Correct!\n")
                countCorrect += 1
            } else {
                println("Wrong!\nCorrect answer: ${item.correct}\n")
            }
        }

        println("Correct answers: $countCorrect")
        println("Total number of answers: ${items.size}")

        // calculate the percentage of correct answersW
        val percentage = (countCorrect.toDouble() / items.size.toDouble()) * 100
        println("Percentage: %.2f".format(percentage) + "%")

        // according to the percentage, print a message
        when {
            percentage > 100.0 -> println("Impossible, you are a genius!")
            percentage == 100.0 -> println("Perfect! Congratulations!")
            percentage >= 90.0 -> println("Excellent work!")
            percentage >= 80.0 -> println("Great!")
            percentage >= 70.0 -> println("Good job!")
            percentage >= 60.0 -> println("Not bad!")
            else -> println("You need to study more!")
        }

    }
}