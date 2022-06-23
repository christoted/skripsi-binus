package com.example.project_skripsi.core.model.local

import java.util.*

data class CalendarItem(val item: HomeSectionData, val eventTime: Date, val viewType: Int)

data class DayEvent(val eventTime: Date, val viewType: Int)