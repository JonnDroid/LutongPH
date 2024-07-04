package com.example.lutongph.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.lutongph.R
import com.example.lutongph.constant.AppConstant.CHANNEL_ID
import com.example.lutongph.constant.AppConstant.MESSAGE_EXTRA
import com.example.lutongph.constant.AppConstant.NOTIFICATION_ID
import com.example.lutongph.constant.AppConstant.TITLE_EXTRA
import com.example.lutongph.presentation.home.HomeScreen

class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        creteNotificationChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(intent.getStringExtra(MESSAGE_EXTRA)))
            .setContentIntent(getPendingIntent(context))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun creteNotificationChannel(context: Context) {
        val name = "LutongPH"
        val desc = "LutongPH Notification Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = desc
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, HomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }
}

