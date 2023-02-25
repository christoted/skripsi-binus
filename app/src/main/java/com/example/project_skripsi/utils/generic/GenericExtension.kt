package com.example.project_skripsi.utils.generic

import com.prolificinteractive.materialcalendarview.CalendarDay

class GenericExtension {

    companion object {
        fun <T> Iterable<T>.averageOf(selector: (T) -> Int): Int {
            var sum = 0
            var count = 0
            for (element in this) {
                sum += selector(element)
                count++
            }
            return sum / count
        }

        operator fun CalendarDay.compareTo(currentDate: CalendarDay): Int {
            with(this) {
                if (year < currentDate.year) return -1
                else if (year > currentDate.year) return 1

                if (month < currentDate.month) return -1
                else if (month > currentDate.month) return 1

                if (day < currentDate.day) return -1
                else if (day > currentDate.day) return 1
            }
            return 0
        }
    }

}