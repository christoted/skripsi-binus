package com.example.project_skripsi.utils.helper

import java.util.*

class TextHelper {

    companion object {

        fun capitalize(string: String) : String {
            return string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

    }

}