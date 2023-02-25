package com.example.project_skripsi.utils.custom.comparator

import com.example.project_skripsi.core.model.local.CalendarItem
import com.example.project_skripsi.core.model.local.DayEvent
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel

object CalendarComparator {

    val comp = Comparator<DayEvent> { a, b ->
        when {
            a.viewType >= StCalendarViewModel.TYPE_PAYMENT && b.viewType >= StCalendarViewModel.TYPE_PAYMENT -> {
                if (a.viewType < b.viewType) -1
                else 1
            }
            a.viewType >= StCalendarViewModel.TYPE_PAYMENT -> 1
            b.viewType >= StCalendarViewModel.TYPE_PAYMENT -> -1
            else -> {
                if (a.eventTime < b.eventTime) -1
                else 1
            }
        }
    }

    val compData = Comparator<CalendarItem> { a, b ->
        when {
            a.viewType >= StCalendarViewModel.TYPE_PAYMENT && b.viewType >= StCalendarViewModel.TYPE_PAYMENT -> {
                if (a.viewType < b.viewType) -1
                else 1
            }
            a.viewType >= StCalendarViewModel.TYPE_PAYMENT -> 1
            b.viewType >= StCalendarViewModel.TYPE_PAYMENT -> -1
            else -> {
                if (a.eventTime < b.eventTime) -1
                else 1
            }
        }
    }

}