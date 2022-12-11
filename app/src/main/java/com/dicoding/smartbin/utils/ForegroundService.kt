package com.dicoding.smartbin.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dicoding.smartbin.R
import com.dicoding.smartbin.ui.HomeActivity
import com.dicoding.smartbin.ui.message.MessageFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ForegroundService : Service() {
    private val serviceJob = Job()

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val name = intent.getStringExtra("name")
        if (name != null){
            val notification = createNotification(name)
            startForeground(NOTIFICATION_ID, notification)
        }else{
            val notification = createNotification("")
            startForeground(NOTIFICATION_ID, notification)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Log.d(TAG, "onDestroy: Service dihentikan")
    }
    
    private fun createNotification(user: String): Notification{
        val intent = Intent(this, HomeActivity::class.java)
        val pendingFlags: Int = if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingFlags)

        val mNotifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notif_title, user))
            .setContentText(getString(R.string.notif_message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_NAME
            builder.setChannelId(CHANNEL_ID)
            mNotifManager.createNotificationChannel(channel)
        }

        return builder.build()
    }

    companion object {
        private const val CHANNEL_ID = "FULL_NOTIFICATION"
        private const val CHANNEL_NAME = "SmartBin Channel"
        private const val NOTIFICATION_ID = 1
        internal val TAG = ForegroundService::class.java.simpleName
    }
}