package com.reylortechnology.aklimdakihediye.Repo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.NotificationReceiver
import com.reylortechnology.aklimdakihediye.ObserverClasses.NotificationTimer
import com.reylortechnology.aklimdakihediye.models.Enums.ReminderScheduleResult
import com.reylortechnology.aklimdakihediye.util.canScheduleExactAlarms
import java.util.Calendar
import javax.inject.Inject

class NotificationsRepo
    @Inject constructor(
        private val context: Context,
        val notificationsDao: NotificationsDao
    ) {

    private val alarmManager: AlarmManager
        get() = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notifications = notificationsDao.getAllNotifications()

    suspend fun deleteReminder(reminder: List<Notifications>){
        reminder.forEach { cancelAlarm(it) }
        notificationsDao.deleteNotification(reminder)
    }

    /**
     * @param selectedDateMillis etkinlik gününün yerel gece yarısı (DatePickerView bu formatta verir)
     */
    suspend fun scheduleAndSaveNotification(
        selectedDateMillis: Long,
        notifyBeforeDays: Int = 1,
        for_who: String,
        message: String,
        releationship: String,
        event: String
    ): ReminderScheduleResult {
        val now = System.currentTimeMillis()
        if (selectedDateMillis < startOfToday()) return ReminderScheduleResult.DATE_IN_PAST

        val notifyAtMillis = computeNotifyAt(selectedDateMillis, notifyBeforeDays)
        if (notifyAtMillis <= now) return ReminderScheduleResult.NOTIFY_TIME_PASSED

        // "Alarmlar ve hatırlatıcılar" izni ana ekranda istenir. Verilmemişse setAlarm esnek alarma düşer;
        // izin sonradan verilince RescheduleReceiver alarmları tam zamanlı olarak yeniden kurar.
        return try {
            val notification = Notifications(
                for_who = for_who,
                message = message,
                date = selectedDateMillis,
                releationship = releationship,
                event = event,
                notifyAt = notifyAtMillis
            )
            val id = notificationsDao.insertNotification(notification).toInt()
            setAlarm(notification.copy(id = id))
            ReminderScheduleResult.SCHEDULED
        } catch (e: Exception) {
            Log.e("NotificationsRepo", "Hatırlatma kurulamadı", e)
            ReminderScheduleResult.ERROR
        }
    }

    /**
     * Yeniden başlatma veya uygulama açılışında çağrılır: AlarmManager alarmları
     * cihaz yeniden başlayınca silindiği için gelecekteki tüm hatırlatmaları tekrar kurar.
     * Alarm aynı requestCode ile kurulduğu için tekrar tekrar çağırmak güvenlidir.
     */
    suspend fun rescheduleAll() {
        val now = System.currentTimeMillis()
        notificationsDao.getAllNotificationsOnce().forEach { stored ->
            var reminder = stored
            if (reminder.notifyAt == 0L) {
                // v12 öncesi kayıt: kaç gün önce seçildiği saklanmamıştı, varsayılan 1 gün kullanılır
                cancelLegacyAlarm(reminder)
                reminder = reminder.copy(
                    notifyAt = computeNotifyAt(reminder.date, NotificationTimer.ONE_DAY_BEFORE.daysBefore)
                )
                notificationsDao.updateNotification(reminder)
            }
            if (reminder.notifyAt > now) setAlarm(reminder)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(reminder: Notifications) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_ID, reminder.id)
            putExtra(NotificationReceiver.EXTRA_TITLE, reminder.event)
            putExtra(NotificationReceiver.EXTRA_MESSAGE, reminder.message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // İzin yoksa hatırlatma kaybolmasın diye esnek alarma düş
        if (canScheduleExactAlarms(context)) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.notifyAt, pendingIntent)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.notifyAt, pendingIntent)
        }
    }

    private fun cancelAlarm(reminder: Notifications) {
        if (reminder.notifyAt == 0L) {
            cancelLegacyAlarm(reminder)
        } else {
            cancelPendingIntent(reminder.id)
        }
    }

    // v12 öncesi alarmlar requestCode olarak tarihin Int halini kullanıyordu
    private fun cancelLegacyAlarm(reminder: Notifications) {
        cancelPendingIntent(reminder.date.toInt())
    }

    private fun cancelPendingIntent(requestCode: Int) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) ?: return
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun computeNotifyAt(dateMillis: Long, notifyBeforeDays: Int): Long =
        Calendar.getInstance().apply {
            timeInMillis = dateMillis
            add(Calendar.DAY_OF_YEAR, -notifyBeforeDays)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    private fun startOfToday(): Long =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
}
