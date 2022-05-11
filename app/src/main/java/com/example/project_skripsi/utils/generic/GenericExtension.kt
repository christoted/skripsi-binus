package com.example.project_skripsi.utils.generic

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

    }

}