package com.example.quizapp.models

enum class QuestionType {
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    TRUE_FALSE
}

enum class QuestionDifficulty {
    EASY,
    MEDIUM,
    HARD,
    NOT_DEFINED
}

data class Item(val category: String = "Not defined",
                val type: Int,
                val difficulty: QuestionDifficulty = QuestionDifficulty.NOT_DEFINED,
                val question : String,
                val answers: MutableList<String>,
                var correct: MutableList<String>)
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
        category = "History",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.MEDIUM,
        question = "What Russian automatic gas-operated assault rifle was developed in the Soviet Union in 1947, and is still popularly used today?",
        answers = mutableListOf(
            "AK-47",
            "RPK",
            "M16",
            "MG 42"
        ),
        correct = mutableListOf("AK-47")
    ),
    Item(
        category = "Entertainment: Music",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.MEDIUM,
        question = "Which singer was featured in Swedish producer Avicii's song 'Wake Me Up'?",
        answers = mutableListOf(
            "Aloe Blacc",
            "John Legend",
            "CeeLo Green",
            "Pharrell Williams"
        ),
        correct = mutableListOf("Aloe Blacc")
    ),
    Item(
        category = "History",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.EASY,
        question = "Which of the following ancient peoples was NOT classified as Hellenic (Greek)?",
        answers = mutableListOf(
            "Illyrians",
            "Dorians",
            "Achaeans",
            "Ionians"
        ),
        correct = mutableListOf("Illyrians")
    ),
    Item(
        category = "Entertainment: Video Games",
        type = QuestionType.TRUE_FALSE.ordinal,
        difficulty = QuestionDifficulty.EASY,
        question = "The main playable character of the 2015 RPG 'Undertale' is a monster.",
        answers = mutableListOf(
            "True",
            "False"
        ),
        correct = mutableListOf("True")
    ),
    Item(
        category = "Entertainment: Video Games",
        type = QuestionType.TRUE_FALSE.ordinal,
        difficulty = QuestionDifficulty.EASY,
        question = "Ana was added as a new hero for the game Overwatch on July 19th, 2016.",
        answers = mutableListOf(
            "True",
            "False"
        ),
        correct = mutableListOf("False")
    ),
    Item(
        category = "Sports",
        type = QuestionType.TRUE_FALSE.ordinal,
        difficulty = QuestionDifficulty.EASY,
        question = "Roger Federer is a famous soccer player.",
        answers = mutableListOf(
            "True",
            "False"
        ),
        correct = mutableListOf("True")
    ),
    Item (
        category = "Entertainment: Music",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.EASY,
        question = "What album did Bon Iver release in 2016?",
        answers = mutableListOf(
            "22, A Million",
            "Bon Iver, Bon Iver",
            "Blood Bank EP",
            "For Emma, Forever Ago"
        ),
        correct = mutableListOf("22, A Million")
    ),
    Item (
        category = "Geography",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.MEDIUM,
        question = "What is the capital of Seychelles?",
        answers = mutableListOf(
            "Victoria",
            "Luanda",
            "N'Djamena",
            "Tripoli"
        ),
        correct = mutableListOf("Victoria")
    ),
    Item(
        category = "Entertainment: Music",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.HARD,
        question = "What is the opening track on Lorde's Pure Heroine?",
        answers = mutableListOf(
            "Tennis Court",
            "Team",
            "Royals",
            "400 Lux"
        ),
        correct = mutableListOf("Tennis Court")
    ),
    Item(
        category = "Science & Nature",
        type = QuestionType.SINGLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.HARD,
        question = "What causes the sound of a heartbeat?",
        answers = mutableListOf(
            "Contraction of the heart chambers",
            "Blood exiting the heart",
            "Relaxation of the heart chambers",
            "Closure of the heart valves"
        ),
        correct = mutableListOf("Closure of the heart valves")
    ),
    Item(
        category = "Kotlin fundamentals",
        type = QuestionType.MULTIPLE_CHOICE.ordinal,
        difficulty = QuestionDifficulty.EASY,
        question = "Which of the following are valid ways to declare a variable in Kotlin?",
        answers = mutableListOf(
            "var x = 5",
            "val x = 5",
            "x = 5",
            "var x: Int = 5"
        ),
        correct = mutableListOf( "var x = 5", "val x = 5", "var x: Int = 5")
    ),
    Item (
        category = "Kotlin fundamentals",
        type = QuestionType.MULTIPLE_CHOICE.ordinal,
        question = "Which of the following are valid ways to declare a function in Kotlin?",
        answers = mutableListOf(
            "fun x() {}",
            "fun x: Int {}",
            "fun x(): Int {}",
            "fun x: Int() {}"
        ),
        correct = mutableListOf( "fun x() {}",  "fun x(): Int {}")
    )
)

val items_old : MutableList<Item> = mutableListOf(
    Item(
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "What is the difference between val and var in Kotlin?",
        answers = mutableListOf(
            "Variables declared with var are final, those with val are not",
            "Variables declared with val can only access const members",
            "var is scoped to the nearest function block and val is scoped to the nearest enclosing block",
            "Variables declared with val are final, those with var are not"
        ),
        correct = mutableListOf( "Variables declared with val are final, those with var are not")
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
        correct = mutableListOf( "var x = 5", "val x = 5", "var x: Int = 5")
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
        correct = mutableListOf( "fun x() {}",  "fun x(): Int {}")
    ),
    Item (
        type = QuestionType.SINGLE_CHOICE.ordinal,
        question = "You can create an emulator to stimulate the configuration of a particular type of Android device using a tool like ____",
        answers = mutableListOf(
            "Theme Editor",
            "AVD Manager",
            "Android SDK Manager",
            "Virtual Editor"
        ),
        correct = mutableListOf( "Theme Editor")
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
        correct = mutableListOf("A class is a blueprint for an object, and an object is an instance of a class")
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
        correct = mutableListOf("Automatic conversion to JSON")
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
        correct = mutableListOf("LLVM")
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
        correct = mutableListOf("They provide asynchronous code without thread blocking")
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
        correct = mutableListOf("var i : Int = 42")
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
        correct = mutableListOf("No, not at language level. But the Kotlin compiler makes use of JVM primitives for best performance")
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
        correct = mutableListOf("Kotlin can easily call Java code and vice versa")
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
        correct = mutableListOf("fun sum(a: Int, b: Int): Int")
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
        correct = mutableListOf("It converts any value to a non-null type and throws an exception if the values is in fact null")
    )
)