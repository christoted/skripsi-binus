package com.example.project_skripsi.utils.helper

import android.annotation.SuppressLint
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

//        const val EDMY = "EEEE, dd - MM - yyyy"
        const val E = "EEEE"
        const val DMY = "dd - MM - yyyy"
        const val hm = "HH:mm"
        val mapWeekDay = mapOf(
            "Monday" to "Senin",
            "Tuesday" to "Selasa",
            "Wednesday" to "Rabu",
            "Thursday" to "Kamis",
            "Friday" to "Jumat",
            "Saturday" to "Sabtu",
            "Sunday" to "Minggu",
        )

        fun getCurrentDate() : Date = Calendar.getInstance().time

        fun getCurrentDateNow(): CalendarDay = CalendarDay.today()

        // TODO: Create a function that take Date as parameter and return Calendar Day
        fun convertDateToCalendarDay(date: Date): CalendarDay {
            return CalendarDay.from(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun getFormattedDateTime(format: String?, date: Date): String? {
            return SimpleDateFormat(format).format(date.time)
        }

        @SuppressLint("SimpleDateFormat")
        fun getFormattedDateTimeWithWeekDay(date: Date): String? {
            val weekday = mapWeekDay[getFormattedDateTime(E, date)]?:""
            return "$weekday, ${getFormattedDateTime(DMY, date)}"
        }

        fun updateDate(date : Date, time : Long) : Date{
            val currentDate = Calendar.getInstance()
            currentDate.time = date

            val newDate = Calendar.getInstance()
            newDate.time = Date(time)

            currentDate.set(Calendar.YEAR, newDate.get(Calendar.YEAR))
            currentDate.set(Calendar.MONTH, newDate.get(Calendar.MONTH))
            currentDate.set(Calendar.DATE, newDate.get(Calendar.DATE))

            return currentDate.time
        }

        fun updateTime(date : Date, hour : Int, minute : Int) : Date{
            val currentDate = Calendar.getInstance()
            currentDate.time = date

            currentDate.set(Calendar.HOUR_OF_DAY, hour)
            currentDate.set(Calendar.MINUTE, minute)

            return currentDate.time
        }

        fun getTomorrow() : Date{
            val c = Calendar.getInstance()
            c.time = Date()
            c.add(Calendar.DATE, 1)
            return c.time
        }


    }


}