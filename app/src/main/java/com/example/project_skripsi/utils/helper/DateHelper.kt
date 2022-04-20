package com.example.project_skripsi.utils.helper

import android.annotation.SuppressLint
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

        const val DMY = "dd - MM - yyyy"
        const val hm = "HH:mm"

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




    }


}