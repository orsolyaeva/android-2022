import java.util.*

fun main(args: Array<String>) {
//    primeNumbersInRange(IntRange(1, 100))
//    printEvenNumbers((1..16).toList())
    testMap()
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
//    printList(daysOfWeek)
//    daysStartingWithLetter(daysOfWeek, "T")
//    daysContainingLetter(daysOfWeek, "e")
    daysGivenLength(daysOfWeek, 6)
}

fun printList(list: List<String>) {
    list.forEach(::println)
}

fun daysStartingWithLetter(list: List<String>, letter: String) {
   list.filter { it.startsWith(letter) }.forEach { println(it) }
}

fun daysContainingLetter(list: List<String>, letter: String) {
    list.filter { it.contains(letter) }.forEach { println(it) }
}

fun daysGivenLength(list: List<String>, length: Int) {
    list.filter { it.length == length }.forEach { println(it) }
}


// exercise 3
fun primeNumbersInRange(range: IntRange) {
    range.filter { isPrime(it) }.forEach { println(it) }
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
//    numbers.map { it * 2 }.forEach{ print("$it ") }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    daysOfWeek.map { it.uppercase() }.forEach { print("$it ") }
}



