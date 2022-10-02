import java.text.SimpleDateFormat
import java.util.Calendar

data class Date(val year: Int, val month: Int, val day: Int) : Comparable<Date> {
    override fun compareTo(other: Date): Int {
        return when {
            year != other.year -> year - other.year
            month != other.month -> month - other.month
            else -> day - other.day
        }
    }

    override fun toString(): String {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, day)
        return SimpleDateFormat("yyyy-MM-dd").format(cal.time)
    }
}

val dateComparator = Comparator<Date> { d1, d2 ->
    when {
        d1.year != d2.year -> d2.year - d1.year
        d1.month != d2.month -> d2.month - d1.month
        else -> d2.day - d1.day
    }
}

fun Date() = Date(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

fun Date.isLeapYear() = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

fun Date.isValid() : Boolean {
    if (this.month < 1 || this.month > 12) return false
    if (this.day < 1 || this.day > 31) return false
    if (this.month == 2 && this.day > 29) return false
    if (this.month == 2 && this.day == 29 && !this.isLeapYear()) return false
    if (this.month in listOf(4, 6, 9, 11) && this.day > 30) return false
    return true
}


