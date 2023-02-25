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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.project_skripsi.R
import com.example.project_skripsi.core.model.firestore.ClassMeeting
import com.example.project_skripsi.core.model.firestore.TaskForm
import com.example.project_skripsi.core.model.local.TeacherAgendaMeeting
import com.example.project_skripsi.core.model.local.TeacherAgendaTaskForm
import com.example.project_skripsi.module.common.auth.AuthActivity
import com.example.project_skripsi.utils.helper.DateHelper
import com.example.project_skripsi.utils.service.storage.StorageSP
import java.util.*

class NotificationUtil(base: Context) : ContextWrapper(base) {


    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    companion object {
        private const val CHANNEL_ID = "App Alert Notification ID"
        private const val CHANNEL_NAME = "App Alert Notification"

        private fun getHashAlarmId(id: String?) = id.hashCode()

        fun scheduleDailyNotification(context: Context, isStudent: Boolean) {
            if (StorageSP.getBoolean(context, StorageSP.SP_DAILY_NOTIFICATION, false)) return
            StorageSP.setBoolean(context, StorageSP.SP_DAILY_NOTIFICATION, true)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 6)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (calendar.time < DateHelper.getCurrentTime()) calendar.add(Calendar.DATE, 1)

            val notificationId = getHashAlarmId("dailyNotification")
            val intent = NotificationReceiver.inst.newIntent(
                context,
                true,
                isStudent,
                notificationId,
                "",
                ""
            )

            Log.d("12345-", "SET $calendar")

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

            Toast.makeText(context, "Daily notification set", Toast.LENGTH_SHORT).show()
        }

        fun scheduleSingleNotification(
            context: Context,
            date: Date,
            title: String,
            body: String,
            id: String
        ) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java)
            if (DateHelper.convertToCalendarDayBeforeStart(date).timeInMillis < DateHelper.convertDateToCalendar(
                    DateHelper.getCurrentTime()
                ).timeInMillis
            ) {
                Log.d("987", "already passed current day $date")
                cancelNotification(context, id)
            } else {
                val timeInMillis = DateHelper.convertToCalendarDayBeforeStart(date).timeInMillis
                intent.putExtra("timeinmillis", timeInMillis)
                intent.putExtra("title", title)
                intent.putExtra("body", body)
                Log.d("987", "date $date")
                val notificationId = getHashAlarmId(id)
                intent.putExtra("id", notificationId)
                val pendingIntent = PendingIntent.getBroadcast(
                    context, notificationId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                Log.d("5555", "scheduleSingleNotification: $notificationId $date")
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    DateHelper.convertToCalendarDayBeforeStart(date).timeInMillis,
                    pendingIntent
                )
            }
        }

        fun cancelNotification(context: Context, id: String) {
              val intent = Intent(context, NotificationReceiver::class.java)
              val notificationId = getHashAlarmId(id)
              val pending = PendingIntent.getBroadcast(
                  context,
                  notificationId,
                  intent,
                  PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
              )
              Log.d("123455", "cancelNotification: on ")
              // Cancel notification
              val manager = context.getSystemService(ALARM_SERVICE) as AlarmManager
              manager.cancel(pending)
        }


        fun cancelDailyNotification(context: Context, isStudent: Boolean) {
            StorageSP.setBoolean(context, StorageSP.SP_DAILY_NOTIFICATION, false)
            val notificationId = getHashAlarmId("dailyNotification")
            val intent = NotificationReceiver.inst.newIntent(
                context,
                true,
                isStudent,
                notificationId,
                "",
                ""
            )
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }

        fun cancelAllMeetingNotification(context: Context, meetings: List<ClassMeeting>) {
            meetings.forEach { classMeeting ->
                classMeeting.startTime?.let {
                    cancelNotification(context, id = classMeeting.id!! + "notif")
                }
            }
        }

        fun cancelAllMeetingNotificationTeacher(
            context: Context,
            meetings: List<TeacherAgendaMeeting>
        ) {
            meetings.forEach { teacherAgenda ->
                teacherAgenda.classMeeting.startTime?.let {
                    cancelNotification(context, id = teacherAgenda.classMeeting.id!!)
                }
            }
        }

        fun cancelAllExamAndAssignmentNotification(context: Context, exams: List<TaskForm>) {
            exams.forEach { taskForm ->
                taskForm.startTime?.let {
                    cancelNotification(context, id = taskForm.id!! + "start")
                }
                taskForm.endTime?.let {
                    cancelNotification(context, id = taskForm.id!! + "end")
                }
            }
        }

        fun cancelAllExamAndAssignmentNotificationTeacher(
            context: Context,
            exams: List<TeacherAgendaTaskForm>
        ) {
            exams.forEach { taskForm ->
                taskForm.taskForm.startTime?.let {
                    cancelNotification(context, id = taskForm.taskForm.id!! + "start")
                }
                taskForm.taskForm.endTime?.let {
                    cancelNotification(context, id = taskForm.taskForm.id!! + "end")
                }
            }
        }
    }

    // Create channel for Android version 26+
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (manager == null) manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(title: String, body: String): NotificationCompat.Builder {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(AuthActivity::class.java)
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