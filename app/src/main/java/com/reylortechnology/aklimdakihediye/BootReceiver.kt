package com.reylortechnology.aklimdakihediye

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

// Cihaz yeniden başlayınca AlarmManager alarmları silinir; burada tekrar kurulur.
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationsRepo: NotificationsRepo

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationsRepo.rescheduleAll()
            } catch (e: Exception) {
                Log.e("BootReceiver", "Hatırlatmalar yeniden kurulamadı", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
