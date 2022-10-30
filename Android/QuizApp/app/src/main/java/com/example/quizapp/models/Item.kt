package com.example.quizapp.models

enum class QuestionType {
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    SPINNER
}

data class Item(val type: Int, val question : String, val answers: MutableList<String>, var correct: MutableList<Int>)
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
        result = 31 * result + correct.hashCode()
        return result
    }
}

val items : MutableList<Item> = mutableListOf(
    Item(
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What is the difference between val and var in Kotlin?",
        answers = mutableListOf(
            "Variables declared with var are final, those with val are not",
            "Variables declared with val can only access const members",
            "var is scoped to the nearest function block and val is scoped to the nearest enclosing block",
            "Variables declared with val are final, those with var are not"
        ),
        correct = mutableListOf(3)
    ),
    Item(
        type = QuestionType.MULTIPLE_CHOICE.ordinal,
        question = "Which of the following are valid ways to declare a variable in Kotlin?",
        answers = mutableListOf(
            "var x = 5",
            "val x = 5",
            "x = 5",
            "var x: Int = 5"
        ),
        correct = mutableListOf(0, 1, 3)
    ),
    Item (
        type = QuestionType.MULTIPLE_CHOICE.ordinal,
        question = "Which of the following are valid ways to declare a function in Kotlin?",
        answers = mutableListOf(
            "fun x() {}",
            "fun x: Int {}",
            "fun x(): Int {}",
            "fun x: Int() {}"
            ),
        correct = mutableListOf(0, 2)
    ),
    Item (
        type = QuestionType.SPINNER.ordinal,
        question = "You can create an emulator to stimulate the configuration of a particular type of Android device using a tool like ____",
        answers = mutableListOf(
            "Theme Editor",
            "AVD Manager",
            "Android SDK Manager",
            "Virtual Editor"
            ),
        correct = mutableListOf(0)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What is the difference between a class and an object in Kotlin?",
        answers = mutableListOf(
            "A class is a blueprint for an object",
            "An object is a blueprint for a class",
            "A class is a blueprint for an object, and an object is an instance of a class",
            "A class is a blueprint for an object, and an object is a class"
        ),
        correct = mutableListOf(2)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What does a data class not offer?",
        answers = mutableListOf(
            "An auto-generated toString() method",
            "Auto-generated hashCode() and equals() methods",
            "A generated copy(...) method, to create copies of instances",
            "Automatic conversion to JSON"
            ),
        correct = mutableListOf(3)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "Which of these targets does Kotlin currently not support?",
        answers = mutableListOf(
            "JavaScript",
            ".NET CLR",
            "JVM",
            "LLVM"
            ),
        correct = mutableListOf(3)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What are Kotlin coroutines?",
        answers = mutableListOf(
            "That's how the automatically generated methods hashCode() and equals() in data classes are called",
            "Its Kotlin's term for class methods",
            "They provide asynchronous code without thread blocking",
            "These are functions which accept other functions as arguments or return them"
            ),
        correct = mutableListOf(2)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What is the correct way to declare a variable of integer type in Kotlin?",
        answers = mutableListOf(
            "let i = 42",
            "var i : Int = 42",
            "var i : int = 42",
            "int i = 42"
            ),
        correct = mutableListOf(1)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "Does Kotlin have primitive data types, such as int, long, float?",
        answers = mutableListOf(
            "Yes, but Kotlin internally always converts them to their non-primitive counterparts",
            "No, not at language level. But the Kotlin compiler makes use of JVM primitives for best performance",
            "Yes, Kotlin is similar to Java in this respect",
            "No, Kotlin does not have nor user primitive data types"
            ),
        correct = mutableListOf(1)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What about Java interoperability?",
        answers = mutableListOf(
            "Kotlin can easily call Java code while Java cannot access code written in Kotlin",
            "Kotlin provides a compatibility layer for Java interoperability which comes with some cost at runtime",
            "Kotlin can easily call Java code and vice versa",
            "While Kotlin runs on the JVM, it cannot interoperate with Java"
            ),
        correct = mutableListOf(2)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "Which is a valid function declaration in Kotlin?",
        answers = mutableListOf(
            "fun sum(a: Int, b: Int): Int",
            "function sum(a: Int, b: Int): Int",
            "int sum(a: Int, b: Int)",
            "int sum(int a, int b)"
            ),
        correct = mutableListOf(0)
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What does the !! operator do?",
        answers = mutableListOf(
            "It converts any value to a non-null type and throws an exception if the values is in fact null",
            "It compares two values for identity rather than equality",
            "It returns the left-hand operand if the operand is not null, otherwise it returns the right hand operand",
            "It's the modulo operator in Kotlin, similar to Java's %"
            ),
        correct = mutableListOf(0)
    )
)