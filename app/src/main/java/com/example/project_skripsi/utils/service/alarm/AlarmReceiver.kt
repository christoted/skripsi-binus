package com.example.project_skripsi.utils.service.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.notification.NotificationUtil

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val timeInMillis = bundle?.getLong("timeinmillis") ?: 0
        val notificationId = NotificationUtil.createNotificationId(timeInMillis)
        val notificationUtil = NotificationUtil(context)
        val notification = notificationUtil.getNotificationBuilder().build()
        notificationUtil.getManager().notify(notificationId, notification)
    }
}