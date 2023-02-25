package com.example.project_skripsi.utils.decorator

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import androidx.core.content.res.ResourcesCompat
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.local.DayEvent
import com.example.project_skripsi.module.student.main.calendar.StCalendarViewModel
import com.example.project_skripsi.utils.app.App
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecorator(private val eventDay: CalendarDay, private val dayEventList: List<DayEvent>) :
    DayViewDecorator {

    val color = mapOf(
        StCalendarViewModel.TYPE_MEETING to ResourcesCompat.getColor(
            App.res!!,
            R.color.indicator_meeting,
            null
        ),
        StCalendarViewModel.TYPE_EXAM to ResourcesCompat.getColor(
            App.res!!,
            R.color.indicator_exam,
            null
        ),
        StCalendarViewModel.TYPE_ASSIGNMENT to ResourcesCompat.getColor(
            App.res!!,
            R.color.indicator_assignment,
            null
        ),
        StCalendarViewModel.TYPE_PAYMENT to ResourcesCompat.getColor(
            App.res!!,
            R.color.indicator_payment,
            null
        ),
        StCalendarViewModel.TYPE_ANNOUNCEMENT to ResourcesCompat.getColor(
            App.res!!,
            R.color.indicator_announcement,
            null
        ),
        StCalendarViewModel.TYPE_MORE to ResourcesCompat.getColor(
            App.res!!,
            R.color.indicator_black,
            null
        ),
    )

    companion object {
        const val EVENT_LIMIT = 4
        private const val INDICATOR_START = 7
        const val INDICATOR_DISTANCE = 14
        val startX = { x: Int -> INDICATOR_START - x * INDICATOR_START }
        const val startY = 10
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean =
        day == eventDay


    override fun decorate(view: DayViewFacade?) {

        if (dayEventList.size > EVENT_LIMIT) {
            var sx = startX(4)
            for (i in 0..3) {
                val it = dayEventList[i]
                val color =
                    if (i == EVENT_LIMIT - 1) color[StCalendarViewModel.TYPE_MORE] else color[it.viewType]
                val span: LineBackgroundSpan =
                    CustomSpan(color!!, sx, startY, (i == EVENT_LIMIT - 1))
                view!!.addSpan(span)
                sx += INDICATOR_DISTANCE
            }
        } else {
            var sx = startX(dayEventList.size)
            dayEventList.map {
                val span: LineBackgroundSpan = CustomSpan(color[it.viewType]!!, sx, startY, false)
                view!!.addSpan(span)
                sx += INDICATOR_DISTANCE
            }
        }
    }

    private class CustomSpan(
        private val color: Int,
        private val xOffset: Int,
        private val yOffset: Int,
        private val isPlus: Boolean
    ) :
        DotSpan() {

        companion object {
            const val DOT_RADIUS = 5f
            const val STROKE_WIDTH = 3f
        }

        override fun drawBackground(
            canvas: Canvas, paint: Paint, left: Int, right: Int, top: Int, baseline: Int,
            bottom: Int, text: CharSequence, start: Int, end: Int, lnum: Int
        ) {
            val oldColor = paint.color
            if (color != 0) paint.color = color

            val mid = (left + right) / 2

            if (isPlus) drawPlus(canvas, mid, bottom, paint)
            else drawCircle(canvas, mid, bottom, paint)

            paint.color = oldColor
        }

        private fun drawCircle(canvas: Canvas, mid: Int, bottom: Int, paint: Paint) {
            canvas.drawCircle(
                (mid + xOffset).toFloat(),
                (bottom + yOffset).toFloat(),
                DOT_RADIUS,
                paint
            )
        }

        private fun drawPlus(canvas: Canvas, mid: Int, bottom: Int, paint: Paint) {
            paint.strokeWidth = STROKE_WIDTH
            canvas.drawLine(
                (mid + xOffset).toFloat(),
                (bottom + yOffset - 5).toFloat(),
                (mid + xOffset).toFloat(),
                (bottom + yOffset + 5).toFloat(),
                paint
            )
            canvas.drawLine(
                (mid + xOffset - 5).toFloat(),
                (bottom + yOffset).toFloat(),
                (mid + xOffset + 5).toFloat(),
                (bottom + yOffset).toFloat(),
                paint
            )
        }
    }
}



