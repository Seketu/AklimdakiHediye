package com.reylortechnology.aklimdakihediye

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.reylortechnology.aklimdakihediye.Repo.NotificationsRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hatırlatma alarmlarını yeniden kurar:
 * - BOOT_COMPLETED: cihaz yeniden başlayınca AlarmManager alarmları silinir.
 * - SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED: "Alarmlar ve hatırlatıcılar" izni verilince
 *   esnek kurulmuş alarmlar tam zamanlı olarak yeniden kurulur.
 */
@AndroidEntryPoint
class RescheduleReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationsRepo: NotificationsRepo

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
        ) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationsRepo.rescheduleAll()
            } catch (e: Exception) {
                Log.e("RescheduleReceiver", "Hatırlatmalar yeniden kurulamadı", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
