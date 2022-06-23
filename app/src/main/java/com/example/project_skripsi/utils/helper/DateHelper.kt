package com.example.project_skripsi.utils.helper

import android.annotation.SuppressLint
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {

        private const val EE = "EEEE"
        const val DMY = "dd - MM - yyyy"
        const val hm = "HH:mm"
        private val mapWeekDay = mapOf(
            "Monday" to "Senin",
            "Tuesday" to "Selasa",
            "Wednesday" to "Rabu",
            "Thursday" to "Kamis",
            "Friday" to "Jumat",
            "Saturday" to "Sabtu",
            "Sunday" to "Minggu",
        )

        fun getCurrentTime(): Date = Calendar.getInstance().time

        fun getCurrentDate(): CalendarDay = CalendarDay.today()

        // TODO: Create a function that take Date as parameter and return Calendar Day
        fun convertDateToCalendarDay(date: Date?): CalendarDay {
            if (date == null) return getCurrentDate()
            return CalendarDay.from(date)
        }

        fun Date?.getDateWithMinuteOffset(minute: Int): Date {
            val calendar = Calendar.getInstance()
            this?.let { calendar.time = it }
            calendar.add(Calendar.MINUTE, minute)
            return calendar.time
        }

        fun Date?.getDateWithSecondOffset(second: Int): Date {
            val calendar = Calendar.getInstance()
            this?.let { calendar.time = it }
            calendar.add(Calendar.SECOND, second)
            return calendar.time
        }

        fun Date?.getDateWithDayOffset(date: Int): Date {
            val calendar = Calendar.getInstance()
            this?.let { calendar.time = it }
            calendar.add(Calendar.DATE, date)
            return calendar.time
        }

        // MARK: 10 min before start
        fun convertToCalendarDayBeforeStart(date: Date): Calendar {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MINUTE, -10)
            return calendar
        }

        fun convertDateToCalendar(date: Date): Calendar {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }
//        fun convertCalendarDayToDate(date: CalendarDay?): Date {
//            if (date == null) return getCurrentDate()
//            return CalendarDay.from(date)
//        }

        @SuppressLint("SimpleDateFormat")
        fun getFormattedDateTime(format: String?, date: Date?): String? {
            return date?.let { SimpleDateFormat(format).format(it) }
        }

        @SuppressLint("SimpleDateFormat")
        fun getFormattedDateTimeWithWeekDay(date: Date?): String {
            if (date == null) return "null date"
            val weekday = mapWeekDay[getFormattedDateTime(EE, date)] ?: ""
            return "$weekday, ${getFormattedDateTime(DMY, date)}"
        }

        fun updateDate(date: Date, time: Long): Date {
            val currentDate = Calendar.getInstance()
            currentDate.time = date

            val newDate = Calendar.getInstance()
            newDate.time = Date(time)

            currentDate.set(Calendar.YEAR, newDate.get(Calendar.YEAR))
            currentDate.set(Calendar.MONTH, newDate.get(Calendar.MONTH))
            currentDate.set(Calendar.DATE, newDate.get(Calendar.DATE))

            return currentDate.time
        }

        fun updateTime(date: Date, hour: Int, minute: Int): Date {
            val currentDate = Calendar.getInstance()
            currentDate.time = date

            currentDate.set(Calendar.HOUR_OF_DAY, hour)
            currentDate.set(Calendar.MINUTE, minute)
            currentDate.set(Calendar.SECOND, 0)

            return currentDate.time
        }

        fun Date?.getDateWithZeroSecond(): Date {
            val currentDate = Calendar.getInstance()
            this?.let { currentDate.time = it }
            currentDate.set(Calendar.SECOND, 0)
            return currentDate.time
        }

        fun getTomorrow(): Date {
            val c = Calendar.getInstance()
            c.time = Date()
            c.add(Calendar.DATE, 1)
            return c.time
        }

        fun getDuration(startTime: Date?, endTime: Date?): Pair<Float, String> {
            if (startTime == null || endTime == null) return Pair(0f, "menit")

            val minutes = (endTime.time - startTime.time) / (1000 * 60)
            if (minutes <= 180) return Pair(minutes.toFloat(), "menit")

            val hours = minutes / 60f
            if (hours <= 72) return Pair(hours, "jam")

            val days = hours / 24f
            return Pair(days, "hari")
        }

        fun getMinute(startTime: Date?, endTime: Date?): Long {
            if (startTime == null || endTime == null) return 0
            return (endTime.time - startTime.time) / (1000 * 60)
        }

    }

}
