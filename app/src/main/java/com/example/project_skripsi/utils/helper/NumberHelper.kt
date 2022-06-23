package com.example.project_skripsi.utils.helper

class NumberHelper {

    companion object {
        fun isNumberOnly(str : String) : Boolean {
            for (x in str) if (x < '0' || x > '9') return false
            return true
        }
    }

}