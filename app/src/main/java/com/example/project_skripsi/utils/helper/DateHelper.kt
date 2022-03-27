package com.example.project_skripsi.utils.helper

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

        const val DMY = "dd - MM - yyyy"

        fun getCurrentDate() : Date = Calendar.getInstance().time

        @SuppressLint("SimpleDateFormat")
        fun getFormattedDateTime(format: String?, date: Date): String? {
            return SimpleDateFormat(format).format(date.time)
        }

    }


}