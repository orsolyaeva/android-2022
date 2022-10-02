fun main(){
//    exercise1()

//    exercise2()

    exercise3()
}

fun exercise1() {
    val dict: IDictionary = HashSetDictionary
    println("Number of words: ${dict.size()}")
    var word: String?
    while(true){
        print("What to find? ")
        word = readLine()
        if( word.equals("quit")){
            break
        }
        println("Result: ${word?.let { dict.find(it) }}")
    }
}
fun exercise2() {
    val word = "John Smith Arnold"
    println(word.monogram())

    val list = listOf("apple", "pear", "melon")
    println(list.joinToStringSep("#"))

    val list2 = listOf("apple", "pear", "melon", "strawberry")
    println(list2.longestString())
}

fun exercise3() {
    val date = Date()

    println("Invalid dates: ")
    val dates = mutableListOf<Date>()
    while(dates.size < 10){
        val rand = Date((1500..2022).random(), (1..20).random(), (1..36).random())

        if(rand.isValid()){
            dates.add(rand)
        }else{
            println(rand)
        }
    }
    println()

    println("Dates : ")
    dates.forEach{ println(it) }
    println()

    println("Sorted list");
    dates.sort()
    dates.forEach(::println)
    println()

    println("Reversed list:")
    dates.reverse()
    dates.forEach(::println)
    println()

    println("Custom order:")
    dates.sortWith(dateComparator)
    dates.forEach(::println)
    println()
}

fun String.monogram() = this.split(" ").map { it.first() }.joinToString("")

fun List<String>.joinToStringSep(separator: String = ", ") = this.joinToString(separator) { it }

fun List<String>.longestString() = this.maxByOrNull { it.length }