package com.example.project_skripsi.utils.helper

import android.content.Context
import android.widget.Toast

class ValidationHelper {

    companion object {
        fun isStringEmpty(context : Context?, str : String, messageItem : String) : Boolean {
            if (str.isEmpty()) {
                Toast.makeText(context, "$messageItem harus diisi", Toast.LENGTH_SHORT).show()
                return true
            }
            return false
        }

        fun isStringInteger(context : Context?, str : String, messageItem : String) : Boolean {
            if (!NumberHelper.isNumberOnly(str)) {
                Toast.makeText(context, "$messageItem harus terdiri dari angka 0-9", Toast.LENGTH_SHORT).show()
                return true
            }
            return false
        }
    }

}