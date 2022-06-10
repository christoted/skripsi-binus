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
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.AttendedMeeting
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.alarm.AlarmReceiver
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
        fun scheduleSingleNotification(context: Context, date: Date, title: String, body: String) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            var timeInMillis: Long = 0
            timeInMillis =  DateHelper.convertToCalendarDayBeforeStart(
                date
            ).timeInMillis
            intent.putExtra("timeinmillis", timeInMillis)
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            Log.d("987 ", "scheduleSingleNotification: current ${DateHelper.convertDateToCalendar(date)}")
            Log.d("987 ", "scheduleSingleNotification: min ${DateHelper.convertToCalendarDayBeforeStart(date)}")
            Log.d("987", "timeinMilis: ${timeInMillis}")
            val notificationId = createNotificationId(timeInMillis)
            val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, DateHelper.convertToCalendarDayBeforeStart(date).timeInMillis, pendingIntent)
        }
        /*
        // MARK: Meeting
        // TODO: Research 10 minutes before start
        private fun scheduleNotificationMeeting(context: Context, attendedMeeting: AttendedMeeting) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            var timeInMillis: Long = 0
            attendedMeeting.startTime?.let {
                timeInMillis = DateHelper.convertDateToCalendar(it).timeInMillis
                intent.putExtra("timeinmillis", timeInMillis)
                Log.d("987", "scheduleNotification: $timeInMillis")
                val notificationId = createNotificationId(timeInMillis)
                val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, DateHelper.convertDateToCalendar(it).timeInMillis, pendingIntent)
            }
        }
        fun scheduleAllNotificationMeeting(context: Context, attendedMeetings: List<AttendedMeeting>) {
            attendedMeetings.forEach{ meeting ->
                scheduleNotificationMeeting(context, meeting)
            }
        }
       private fun cancelNotificationMeeting(context: Context, attendedMeeting: AttendedMeeting) {
           attendedMeeting.startTime?.let {
               val intent = Intent(context, AlarmReceiver::class.java)
               val timeMillis = DateHelper.convertDateToCalendar(it).timeInMillis
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
       }
        fun cancelAllNotificationMeeting(context: Context, attendedMeetings: List<AttendedMeeting>) {
            attendedMeetings.forEach { meeting ->
                cancelNotificationMeeting(context, meeting)
            }
        }

        // MARK: Exam and Task
        // TODO: Research 10 minutes before start
        private fun scheduleNotificationExamAssignment(context: Context, taskForm: TaskForm) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            var timeInMillis: Long = 0
            taskForm.startTime?.let {
                timeInMillis = DateHelper.convertDateToCalendar(it).timeInMillis
                intent.putExtra("timeinmillis", timeInMillis)
                Log.d("987", "scheduleNotification: $timeInMillis")
                val notificationId = createNotificationId(timeInMillis)
                val pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, DateHelper.convertDateToCalendar(it).timeInMillis, pendingIntent)
            }
        }
        fun scheduleAllNotificationExamAssignment(context: Context, taskForms: List<TaskForm>) {
            taskForms.forEach {
                scheduleNotificationExamAssignment(context, it)
            }
        }

        private fun cancelNotificationExamAssignment(context: Context, taskForm: TaskForm) {
            taskForm.startTime?.let {
                val intent = Intent(context, AlarmReceiver::class.java)
                val timeMillis = DateHelper.convertDateToCalendar(it).timeInMillis
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
        }
        fun cancelAllNotificationExamAssignment(context: Context, taskForms: List<TaskForm>) {
            taskForms.forEach { taskForm ->
                cancelNotificationExamAssignment(context, taskForm)
            }
        }
        */


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
        // TODO: Implement the intent but using navigation graph
        /*
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        */
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setColor(Color.YELLOW)
//            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
    }
}