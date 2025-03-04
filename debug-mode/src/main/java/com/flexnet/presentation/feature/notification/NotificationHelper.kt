package com.flexnet.presentation.feature.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.flexnet.presentation.feature.FlexNetActivity
import javax.inject.Inject

class NotificationHelper @Inject constructor(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "flexnet_notification_channel"
        private const val CHANNEL_NAME = "FlexNet Notifications"
    }

    init {
        createNotificationChannel() // Call this to ensure the notification channel is created
    }

    // Create notification channel for Android 8.0 and higher
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Notifications for FlexNet"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private val intent = Intent(context, FlexNetActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    val pendingIntent =
        PendingIntent.getActivity(context, 1234, intent, PendingIntent.FLAG_IMMUTABLE)

    fun showNotification(title: String, message: String) {
        val notificationId = System.currentTimeMillis().toInt()
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
