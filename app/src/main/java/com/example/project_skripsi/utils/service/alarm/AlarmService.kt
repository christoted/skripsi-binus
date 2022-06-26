package com.example.project_skripsi.utils.service.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.helper.DateHelper.Companion.getDateWithSecondOffset
import java.util.*

class AlarmService {

    companion object {
        val inst = AlarmService()
    }

    // date second must be 55
    fun createAlarm(context: Context, title: String, date: Date, id: String?) {
        cancelAlarm(context, id)
        if (date < DateHelper.getCurrentTime()) return

        val intent = AlarmReceiver.inst.newIntent(context, title, date.getDateWithSecondOffset(5))
        val alarmId = getHashAlarmId(id)
        val pendingIntent = PendingIntent.getBroadcast(
            context, alarmId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("123456-", "Set $id")
        val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            date.time,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, id: String?) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmId = getHashAlarmId(id)
        val pending = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("123456-", "Cancel $id")
        val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        manager.cancel(pending)
    }

    private fun getHashAlarmId(id: String?) = id.hashCode()

}