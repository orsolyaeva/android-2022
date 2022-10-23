package com.example.quizapp.quiz

data class Item(val question : String, val answers: MutableList<String>, var correct: Number)
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
        question = "What is the difference between val and var in Kotlin?",
        answers = mutableListOf(
            "Variables declared with var are final, those with val are not",
            "Variables declared with val can only access const members",
            "var is scoped to the nearest function block and val is scoped to the nearest enclosing block",
            "Variables declared with val are final, those with var are not"
        ),
        correct = 3
    ),
    Item (
        question = "What is the difference between a class and an object in Kotlin?",
        answers = mutableListOf(
            "A class is a blueprint for an object",
            "An object is a blueprint for a class",
            "A class is a blueprint for an object, and an object is an instance of a class",
            "A class is a blueprint for an object, and an object is a class"
        ),
        correct = 2
    ),
    Item (
        question = "What does a data class not offer?",
        answers = mutableListOf(
            "An auto-generated toString() method",
            "Auto-generated hashCode() and equals() methods",
            "A generated copy(...) method, to create copies of instances",
            "Automatic conversion to JSON"
            ),
        correct = 3
    ),
    Item (
        question = "Which of these targets does Kotlin currently not support?",
        answers = mutableListOf(
            "JavaScript",
            ".NET CLR",
            "JVM",
            "LLVM"
            ),
        correct = 1
    ),
    Item (
        question = "What are Kotlin coroutines?",
        answers = mutableListOf(
            "That's how the automatically generated methods hashCode() and equals() in data classes are called",
            "Its Kotlin's term for class methods",
            "They provide asynchronous code without thread blocking",
            "These are functions which accept other functions as arguments or return them"
            ),
        correct = 2
    ),
    Item (
        question = "What is the correct way to declare a variable of integer type in Kotlin?",
        answers = mutableListOf(
            "let i = 42",
            "var i : Int = 42",
            "var i : int = 42",
            "int i = 42"
            ),
        correct = 1
    ),
    Item (
        question = "Does Kotlin have primitive data types, such as int, long, float?",
        answers = mutableListOf(
            "Yes, but Kotlin internally always converts them to their non-primitive counterparts",
            "No, not at language level. But the Kotlin compiler makes use of JVM primitives for best performance",
            "Yes, Kotlin is similar to Java in this respect",
            "No, Kotlin does not have nor user primitive data types"
            ),
        correct = 1
    ),
    Item (
        question = "What about Java interoperability?",
        answers = mutableListOf(
            "Kotlin can easily call Java code while Java cannot access code written in Kotlin",
            "Kotlin provides a compatibility layer for Java interoperability which comes with some cost at runtime",
            "Kotlin can easily call Java code and vice versa",
            "While Kotlin runs on the JVM, it cannot interoperate with Java"
            ),
        correct = 2
    ),
    Item (
        question = "Which is a valid function declaration in Kotlin?",
        answers = mutableListOf(
            "fun sum(a: Int, b: Int): Int",
            "function sum(a: Int, b: Int): Int",
            "int sum(a: Int, b: Int)",
            "int sum(int a, int b)"
            ),
        correct = 0
    ),
    Item (
        question = "What does the !! operator do?",
        answers = mutableListOf(
            "It converts any value to a non-null type and throws an exception if the values is in fact null",
            "It compares two values for identity rather than equality",
            "It returns the left-hand operand if the operand is not null, otherwise it returns the right hand operand",
            "It's the modulo operator in Kotlin, similar to Java's %"
            ),
        correct = 0
    )
)