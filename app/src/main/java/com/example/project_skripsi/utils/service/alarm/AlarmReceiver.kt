package com.example.project_skripsi.utils.service.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import com.example.project_skripsi.utils.helper.DateHelper.Companion.convertDateToCalendar
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        val inst = AlarmReceiver()

        const val EXTRA_TITLE = "title"
        const val EXTRA_HOUR = "hour"
        const val EXTRA_MINUTE = "minute"
    }

    fun newIntent(context: Context, title: String, date: Date): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_HOUR, convertDateToCalendar(date).get(Calendar.HOUR_OF_DAY))
        intent.putExtra(EXTRA_MINUTE, convertDateToCalendar(date).get(Calendar.MINUTE))
        return intent
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.extras?.getString(EXTRA_TITLE) ?: ""
        val hour = intent.extras?.getInt(EXTRA_HOUR) ?: 0
        val minute = intent.extras?.getInt(EXTRA_MINUTE) ?: 0

//        Log.d("12345-", "WOI $title $hour $minute")

        val alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM)
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hour)
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minute)
        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, title)
        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(alarmIntent)
    }
}