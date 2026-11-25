package com.reylortechnology.aklimdakihediye.Repo

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.LocalDatabase
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.NotificationReceiver
import com.reylortechnology.aklimdakihediye.models.Enums.ReminderScheduleResult
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

/**
 * Gerçek AlarmManager ile çalışır; kurulan alarmlar `dumpsys alarm` ile doğrulanır.
 * Test kayıtları 900001+ id kullanır ki uygulamanın gerçek hatırlatmalarıyla çakışmasın.
 * Tüm alarmlar tearDown'da iptal edilir.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsRepoTest {

    private lateinit var context: Context
    private lateinit var db: LocalDatabase
    private lateinit var dao: NotificationsDao
    private lateinit var repo: NotificationsRepo
    private val legacyCodesToCancel = mutableListOf<Int>()

    private val alarmManager: AlarmManager
        get() = context.getSystemService(AlarmManager::class.java)

    @Before
    fun setUp() = runBlocking {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
        dao = db.notificationsDao()
        // AUTOINCREMENT sayacını 900000'e taşı
        val seed = row(id = 900000, date = 0L)
        dao.insertNotification(seed)
        dao.deleteNotification(listOf(seed))
        repo = NotificationsRepo(context, dao)
    }

    @After
    fun tearDown() = runBlocking {
        repo.deleteReminder(dao.getAllNotificationsOnce())
        legacyCodesToCancel.forEach { code ->
            PendingIntent.getBroadcast(
                context, code, Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )?.let { alarmManager.cancel(it); it.cancel() }
        }
        db.close()
    }

    // ---------- Doğrulama kuralları ----------

    @Test
    fun pastDate_isRejected_andNothingSaved() = runBlocking {
        val result = schedule(dateInDays(-1), notifyBeforeDays = 1)
        assertEquals(ReminderScheduleResult.DATE_IN_PAST, result)
        assertTrue(dao.getAllNotificationsOnce().isEmpty())
    }

    @Test
    fun notifyTimeAlreadyPassed_isRejected_andNothingSaved() = runBlocking {
        // Yarınki etkinlik için "1 hafta önce" → bildirim anı geçmişte
        val result = schedule(dateInDays(1), notifyBeforeDays = 7)
        assertEquals(ReminderScheduleResult.NOTIFY_TIME_PASSED, result)
        assertTrue(dao.getAllNotificationsOnce().isEmpty())
    }

    @Test
    fun scheduleAndSave_setsAlarmAtNineOClock_withOrWithoutExactPermission() = runBlocking {
        // İzin yoksa esnek alarm kurulur; her iki durumda da alarm 09:00'a kurulmalı
        val date = dateInDays(40)
        assertEquals(ReminderScheduleResult.SCHEDULED, schedule(date, notifyBeforeDays = 3))

        val saved = dao.getAllNotificationsOnce().single()
        assertEquals(expectedNotifyAt(date, 3), saved.notifyAt)
        assertEquals(1, alarmCountAt(saved.notifyAt))
    }

    // ---------- Aynı gün çakışması ve silme ----------

    @Test
    fun twoRemindersOnSameDate_bothGetTheirOwnAlarm() = runBlocking {
        val date = dateInDays(41)
        val notifyAt = expectedNotifyAt(date, 1)
        insert(row(date = date, notifyAt = notifyAt))
        insert(row(date = date, notifyAt = notifyAt))

        repo.rescheduleAll()

        assertEquals("İki ayrı alarm olmalı", 2, alarmCountAt(notifyAt))
    }

    @Test
    fun deletingReminder_cancelsOnlyItsAlarm() = runBlocking {
        val date = dateInDays(42)
        val notifyAt = expectedNotifyAt(date, 1)
        val first = insert(row(date = date, notifyAt = notifyAt))
        insert(row(date = date, notifyAt = notifyAt))
        repo.rescheduleAll()
        assertEquals(2, alarmCountAt(notifyAt))

        repo.deleteReminder(listOf(first))

        assertEquals("Silinenin alarmı iptal edilmeli, diğeri kalmalı", 1, alarmCountAt(notifyAt))
        assertEquals(1, dao.getAllNotificationsOnce().size)
    }

    // ---------- Yeniden kurma (boot / uygulama açılışı) ----------

    @Test
    fun rescheduleAll_restoresAlarmsAfterTheyWereCleared() = runBlocking {
        val date = dateInDays(43)
        val notifyAt = expectedNotifyAt(date, 2)
        val saved = insert(row(date = date, notifyAt = notifyAt))
        repo.rescheduleAll()
        assertEquals(1, alarmCountAt(notifyAt))

        // Yeniden başlatmayı taklit et: sistem alarmı silmiş gibi
        PendingIntent.getBroadcast(
            context, saved.id, Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )!!.let { alarmManager.cancel(it); it.cancel() }
        assertEquals(0, alarmCountAt(notifyAt))

        repo.rescheduleAll()
        assertEquals(1, alarmCountAt(notifyAt))
    }

    @Test
    fun rescheduleAll_isIdempotent() = runBlocking {
        val date = dateInDays(44)
        val notifyAt = expectedNotifyAt(date, 1)
        insert(row(date = date, notifyAt = notifyAt))
        repo.rescheduleAll()
        repo.rescheduleAll()
        repo.rescheduleAll()
        assertEquals(1, alarmCountAt(notifyAt))
    }

    @Test
    fun rescheduleAll_skipsRemindersWhoseTimePassed() = runBlocking {
        val pastNotifyAt = System.currentTimeMillis() - 60 * 60 * 1000
        insert(row(date = dateInDays(0), notifyAt = pastNotifyAt))
        repo.rescheduleAll()
        assertEquals(0, alarmCountAt(pastNotifyAt))
    }

    @Test
    fun legacyReminder_isConvertedAndOldAlarmCancelled() = runBlocking {
        val date = dateInDays(45)
        // Eski kodun yaptığı gibi: requestCode = date.toInt(), kullanıcı "1 hafta önce" seçmiş
        val legacyNotifyAt = expectedNotifyAt(date, 7)
        val legacyPi = PendingIntent.getBroadcast(
            context, date.toInt(), Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        legacyCodesToCancel += date.toInt()
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, legacyNotifyAt, legacyPi)
        assertEquals(1, alarmCountAt(legacyNotifyAt))
        insert(row(date = date, notifyAt = 0L))

        repo.rescheduleAll()

        val converted = dao.getAllNotificationsOnce().single()
        assertEquals("Varsayılan 1 gün önce 09:00", expectedNotifyAt(date, 1), converted.notifyAt)
        assertEquals("Eski alarm iptal edilmeli", 0, alarmCountAt(legacyNotifyAt))
        assertEquals("Yeni alarm kurulmalı", 1, alarmCountAt(converted.notifyAt))
    }

    // ---------- Uçtan uca: alarm tetiklenir, bildirim düşer ----------

    @Test
    fun alarmFires_andNotificationIsPosted() = runBlocking {
        assumeTrue("Tam zamanlı alarm izni yok, bu test atlandı", canScheduleExact())
        assumeTrue(
            "Bildirim izni yok, bu test atlandı",
            Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
        val saved = insert(
            row(date = dateInDays(0), notifyAt = System.currentTimeMillis() + 3_000, event = "Test hatırlatma")
        )
        repo.rescheduleAll()

        val nm = context.getSystemService(NotificationManager::class.java)
        val deadline = System.currentTimeMillis() + 30_000
        var posted = false
        while (System.currentTimeMillis() < deadline && !posted) {
            posted = nm.activeNotifications.any { it.id == saved.id }
            if (!posted) Thread.sleep(500)
        }
        nm.cancel(saved.id)
        assertTrue("30 sn içinde bildirim gelmedi", posted)
    }

    // ---------- Yardımcılar ----------

    private suspend fun schedule(date: Long, notifyBeforeDays: Int) =
        repo.scheduleAndSaveNotification(
            selectedDateMillis = date,
            notifyBeforeDays = notifyBeforeDays,
            for_who = "Test",
            message = "test mesajı",
            releationship = "Test",
            event = "Test etkinlik"
        )

    private fun row(id: Int = 0, date: Long, notifyAt: Long = 0L, event: String = "Test etkinlik") =
        Notifications(
            id = id, for_who = "Test", event = event, date = date,
            releationship = "Test", message = "test mesajı", notifyAt = notifyAt
        )

    private suspend fun insert(n: Notifications): Notifications =
        n.copy(id = dao.insertNotification(n).toInt())

    private fun canScheduleExact() =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    private fun dateInDays(days: Int): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, days)
    }.timeInMillis

    private fun expectedNotifyAt(date: Long, daysBefore: Int): Long = Calendar.getInstance().apply {
        timeInMillis = date
        add(Calendar.DAY_OF_YEAR, -daysBefore)
        set(Calendar.HOUR_OF_DAY, 9); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    /** Bu uygulamaya ait, verilen anda tetiklenecek alarm sayısı. */
    private fun alarmCountAt(triggerAtMillis: Long): Int {
        val pfd = InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("dumpsys alarm")
        val dump = ParcelFileDescriptor.AutoCloseInputStream(pfd).bufferedReader().use { it.readText() }
        val pattern = Regex("""origWhen $triggerAtMillis whenElapsed -?\d+ ${Regex.escape(context.packageName)}\}""")
        return pattern.findAll(dump).count()
    }
}
