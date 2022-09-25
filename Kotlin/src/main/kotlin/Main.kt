import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    while(true) {
        print("\nEnter the number of the exercise (0 show options | 1-8 exercises | any other number to exit): ")
        val input = readLine()
        if (input == null || input == "") {
            println("You didn't enter anything!")
            continue
        }

        val number = try {
            input.toInt()
        } catch (e: NumberFormatException) {
            println("'$input' is not a number!")
            continue
        }

        when(number) {
            0 -> showOptions()
            1 -> testingStringTemplate()
            2 -> testList()
            3 -> primeNumbersInRange(IntRange(1, 100))
            4 -> codeMessage()
            5 -> {
                printEvenNumbers((1..25).toList())
                println()
            }
            6 -> testMap()
            7 -> testMutableList()
            8 -> testArrays()
            else -> {
                println("Bye!")
                exitProcess(0)
            }
        }
    }
}

fun showOptions() {
    println("1. Testing String Template")
    println("2. Immutable List")
    println("3. Prime Test")
    println("4. Code Message")
    println("5. Compact Function")
    println("6. Map")
    println("7. Mutable List")
    println("8. Arrays")
}

// exercise 1
fun testingStringTemplate() {
    val number1 = 5
    val number2 = 10
    println("$number1 + $number2 = ${number1 + number2}")
}


// exercise 2
fun testList() {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    printList(daysOfWeek)
    daysStartingWithLetter(daysOfWeek, "T")
    daysContainingLetter(daysOfWeek, "e")
    daysGivenLength(daysOfWeek, 6)
}

fun printList(list: List<String>) {
    println("\nList: ")
    list.forEach(::println)
}

fun daysStartingWithLetter(list: List<String>, letter: String) {
    println("\nDays starting with $letter: ")
    list.filter { it.startsWith(letter) }.forEach { println(it) }
}

fun daysContainingLetter(list: List<String>, letter: String) {
    println("\nDays containing $letter: ")
    list.filter { it.contains(letter) }.forEach { println(it) }
}

fun daysGivenLength(list: List<String>, length: Int) {
    println("\nDays with length $length: ")
    list.filter { it.length == length }.forEach { println(it) }
}


// exercise 3
fun primeNumbersInRange(range: IntRange) {
    println("Prime numbers in range $range: ")
    range.filter { isPrime(it) }.forEach { print("$it ") }
    println()
}

fun isPrime(number: Int): Boolean {
    if (number <= 1) {
        return false
    }
    for (i in 2..number / 2) {
        if (number % i == 0) {
            return false
        }
    }
    return true
}


// exercise 4
fun codeMessage() {
    val message = "android"
    val encodedMessage = encode(message)
    println("Encoded message: $encodedMessage")

    val decodedMessage = decode(encodedMessage)
    println("Decoded message: $decodedMessage")
}

fun messageCoding(msg: String, func: (String) -> String): String {
    return func(msg)
}

fun encode(msg: String): String {
    val encoder: Base64.Encoder = Base64.getEncoder()
    return encoder.encodeToString(msg.toByteArray())
}

fun decode(msg: String): String {
    val decoder: Base64.Decoder = Base64.getDecoder()
    return String(decoder.decode(msg))
}

// exercise 5
fun printEvenNumbers(list: List<Int>) = list.filter { it % 2 == 0 }.forEach { print("$it ") }

// exercise 6
fun testMap() {
    val numbers = (1..10).toList()
    print("\nNumbers: ")
    numbers.forEach{print("$it ")}
    print("\nNumbers squared: ")
    numbers.map { it * 2 }.forEach{ print("$it ") }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    print("\n\nDays of week capitalized: ")
    daysOfWeek.map { it.uppercase() }.forEach { print("$it ") }

    print("\n\nFirst character of each day: ")
    daysOfWeek.map { it.first().lowercase() }.forEach { print("$it ") }

    println("\n\nLength of each day:")
    daysOfWeek.map { Pair(it.length, it) }.forEach { println("${it.first} -> ${it.second} ") }

    val average = daysOfWeek.map { it.length }.average()
    println("\nAverage length of days of the week: $average")
}


// exercise 7

fun testMutableList() {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val mutableDaysOfWeek = daysOfWeek.toMutableList()
    mutableDaysOfWeek.removeAll { it.contains("n") }
    print("Days of week without 'n': ")
    mutableDaysOfWeek.forEach { print("$it ") }

    val indexedMutableList = mutableDaysOfWeek.withIndex()
    println("\n\nIndexed list:")
    indexedMutableList.forEach { println("Item at ${it.index} is ${it.value}") }

    mutableDaysOfWeek.sort()
    println("\nSorted list:")
    mutableDaysOfWeek.forEach { print("$it ") }

    println()
}


// exercise 8
fun testArrays() {
    // generate array of 10 random integers between 0 and 100
    val array = Array(10) { (0..100).random() }
    println("Array: ")
    array.forEach (::println)

    array.sort()
    println("\nSorted array: ")
    array.forEach { print("$it ") }

    val containsEven = array.any { it % 2 == 0 }
    println("\n\nArray contains even number: $containsEven")

    val allEven = array.all { it % 2 == 0 }
    println("All numbers are even: $allEven")

    val average = array.average()
    println("Average of generated numbers: $average")

    // calculate average using forEach
    var sum : Double = 0.0
    array.forEach { sum += it }
    println("Average of generated numbers: ${sum / array.size}")
}


