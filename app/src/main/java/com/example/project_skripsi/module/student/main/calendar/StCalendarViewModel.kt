package com.example.project_skripsi.module.student.main.calendar

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.util.*

//data class

class StCalendarViewModel : ViewModel() {

    companion object {
        const val TYPE_CLASS = 0
        const val TYPE_EXAM = 1
        const val TYPE_ASSIGNMENT = 2
        const val TYPE_PAYMENT = 3
        const val TYPE_ANNOUNCEMENT = 4
        const val TYPE_MORE = 10
    }

    private val _eventList = MutableLiveData<Map<CalendarDay, List<DayEvent>>>()
    val eventList : LiveData<Map<CalendarDay, List<DayEvent>>> = _eventList

    init {

        Handler(Looper.getMainLooper()).postDelayed({
            _eventList.value = mapOf(
                CalendarDay.from(2022, 2, 14) to listOf(
                    DayEvent(Calendar.getInstance().time, TYPE_EXAM),
                ),
                CalendarDay.from(2022, 2, 15) to listOf(
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_EXAM),
                ),
                CalendarDay.from(2022, 2, 16) to listOf(
                    DayEvent(Calendar.getInstance().time, TYPE_CLASS),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_EXAM),
                ),
                CalendarDay.from(2022, 2, 17) to listOf(
                    DayEvent(Calendar.getInstance().time, TYPE_CLASS),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_EXAM),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                ),
                CalendarDay.from(2022, 2, 18) to listOf(
                    DayEvent(Calendar.getInstance().time, TYPE_CLASS),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_EXAM),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_CLASS),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                    DayEvent(Calendar.getInstance().time, TYPE_EXAM),
                    DayEvent(Calendar.getInstance().time, TYPE_PAYMENT),
                ),
            )
        }, 1000)

    }


}