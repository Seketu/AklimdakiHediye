package com.reylortechnology.aklimdakihediye.Repo

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.NotificationReceiver
import java.util.Calendar
import javax.inject.Inject
import androidx.core.net.toUri

class NotificationsRepo
    @Inject constructor(
        val notificationsDao: NotificationsDao
    ) {

    suspend fun deleteReminder(reminder: List<Notifications>){
        notificationsDao.deleteNotification(reminder)
    }

        val notifications = notificationsDao.getAllNotifications()

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    suspend fun scheduleAndSaveNotification(
        context: Context,
        selectedDateMillis: Long,
        notifyBeforeDays: Int = 1,
        for_who: String,
        message: String,
        releationship: String,
        event: String
    ): Boolean {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedDateMillis
            add(Calendar.DAY_OF_YEAR, -notifyBeforeDays)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val notifyAtMillis = calendar.timeInMillis

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", event)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            selectedDateMillis.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intentSettings =
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                            data = "package:${context.packageName}".toUri()
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    context.startActivity(intentSettings)
                    return false
                }
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notifyAtMillis,
                pendingIntent
            )

            val notification = Notifications(
                for_who = for_who,
                message = message,
                date = selectedDateMillis,
                releationship = releationship,
                event = event
            )
            notificationsDao.insertNotification(notification)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
