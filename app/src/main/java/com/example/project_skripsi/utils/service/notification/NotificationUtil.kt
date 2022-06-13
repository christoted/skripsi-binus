package com.example.project_skripsi.utils.service.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.project_skripsi.R
import com.example.project_skripsi.module.student.StMainActivity
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.alarm.AlarmReceiver
import java.time.LocalDateTime
import java.util.*

class NotificationUtil(base: Context) : ContextWrapper(base) {
    val CHANNEL_ID = "App Alert Notification ID"
    val CHANNEL_NAME = "App Alert Notification"

    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }
    companion object {
        fun scheduleEveryDayNotification(context: Context, title: String, body: String) {
            val intent = Intent(context, AlarmReceiver::class.java)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
            }
            intent.putExtra("timeinmillis", calendar.timeInMillis)
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            val notificationId = createNotificationId(calendar.timeInMillis)
            val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        fun scheduleSingleNotification(context: Context, date: Date, title: String, body: String, ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            var timeInMillis: Long = 0
            if (DateHelper.convertToCalendarDayBeforeStart(date).timeInMillis < DateHelper.convertDateToCalendar(DateHelper.getCurrentDate()).timeInMillis) {
                Log.d("987", "already passed current day $date")
                cancelNotification(context, date)
            } else {
                timeInMillis =  DateHelper.convertToCalendarDayBeforeStart(
                    date
                ).timeInMillis
                intent.putExtra("timeinmillis", timeInMillis)
                intent.putExtra("title", title)
                intent.putExtra("body", body)
                Log.d("987", "date $date")
                val notificationId = createNotificationId(timeInMillis)
                val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, DateHelper.convertToCalendarDayBeforeStart(date).timeInMillis, pendingIntent)
            }
        }
        fun cancelNotification(context: Context, date: Date) {
                val intent = Intent(context, AlarmReceiver::class.java)
                val timeMillis = DateHelper.convertDateToCalendar(date).timeInMillis
                val notificationId = createNotificationId(timeMillis)
                val pending = PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                // Cancel notification
                val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager
                manager.cancel(pending)
        }
        fun cancelEveryDayNotification(context: Context) {
            val intent = Intent(context, AlarmReceiver::class.java)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
            }
            val notificationId = createNotificationId(calendar.timeInMillis)
            val pending = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            // Cancel notification
            val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            manager.cancel(pending)
        }
        fun createNotificationId(timeMillis: Long): Int {
            return (timeMillis % 2000000000).toInt()
        }
    }
    // Create channel for Android version 26+
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        getManager().createNotificationChannel(channel)
    }

    fun getManager() : NotificationManager {
        if (manager == null) manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(title: String, body: String): NotificationCompat.Builder {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(StMainActivity::class.java)
            .setGraph(R.navigation.st_main_navigation)
            .setDestination(R.id.navigation_home_fragment)
            .createPendingIntent()

        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setColor(Color.YELLOW)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
    }
}