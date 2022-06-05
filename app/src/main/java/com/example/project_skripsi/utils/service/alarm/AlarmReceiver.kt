package com.example.project_skripsi.utils.service.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.notification.NotificationUtil

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val timeInMillis = bundle?.getLong("timeinmillis") ?: 0
        Log.d("987", "onReceive: is Triggered ${timeInMillis}")
        val notificationId = NotificationUtil.createNotificationId(timeInMillis)
        val notificationUtil = NotificationUtil(context)
        val notification = notificationUtil.getNotificationBuilder().build()
        notificationUtil.getManager().notify(notificationId, notification)
    }
}