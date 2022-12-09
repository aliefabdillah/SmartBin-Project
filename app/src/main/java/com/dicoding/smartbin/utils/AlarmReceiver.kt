package com.dicoding.smartbin.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dicoding.smartbin.R
import com.dicoding.smartbin.ui.HomeActivity
import com.dicoding.smartbin.ui.message.MessageFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        if (message != null && title != null){
            showAlarmNotification(context, title, message)
        }
    }

    private fun showAlarmNotification(context: Context, title: String, message: String) {
        val channelId = "FULL_NOTIFICATION"
        val channelName = "SmartBin Channel"

        val requestId = System.currentTimeMillis()
        val intent = Intent(context, MessageFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestId.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        val mNotifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelName
            builder.setChannelId(channelId)
            mNotifManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        mNotifManager.notify(ID_REPEATING, notification)
    }

    fun setRepeatingAlarm(context: Context, title: String, message: String, time: String){
        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TITLE, title)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)


        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_IMMUTABLE)
        //cancel pending intent ke sistem
        pendingIntent.cancel()

        //membatalkan alarm manager ke sistem
        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Repeating Alarm Canceled", Toast.LENGTH_SHORT).show()

    }

    private fun isDateInvalid(date: String, format: String): Boolean{
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        }catch (e: ParseException){
            true
        }
    }

    companion object{
        //inten key
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TITLE = "title"

        private const val ID_REPEATING = 101

        private const val TIME_FORMAT = "HH:mm"
    }
}