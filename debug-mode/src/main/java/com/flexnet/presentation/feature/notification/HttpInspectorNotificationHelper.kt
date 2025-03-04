package com.flexnet.presentation.feature.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.data.toHttpInspectorState
import com.flexnet.domain.model.HttpInspectorStateInterceptor
import com.flexnet.presentation.feature.FlexNetActivity
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

internal class HttpInspectorNotificationHelper @Inject constructor(
    private val context: Context,
) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "flexnet_http_inspector_notification_channel"
        private const val CHANNEL_NAME = "FlexNet Http Inspector Notifications"
        private const val GROUP_KEY_HTTP_INSPECTOR = "http-inspector"
        private const val SUMMARY_ID = 1000
        const val EXTRA_HTTP_INSPECTOR = "contract.intent.args"
    }

    init {
        createNotificationChannel()
    }

    // Create notification channel for Android 8.0 and higher
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Http Inspector Notifications for FlexNet"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    internal fun showNotification(
        id: Int,
        httpInspectorEntity: HttpInspectorEntity,
    ) {
        val intent = Intent(context, FlexNetActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(
                EXTRA_HTTP_INSPECTOR,
                HttpInspectorNotificationArgs(id = httpInspectorEntity.id.toString()),
            )
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(httpInspectorEntity.httpRequestInspectorEntity?.requestUrl)
            .setContentText(getMessage(httpInspectorEntity))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(GROUP_KEY_HTTP_INSPECTOR)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id, notification)

        createSummaryNotification()
    }

    private fun getMessage(httpInspectorEntity: HttpInspectorEntity): String {
        val state = httpInspectorEntity.state.toHttpInspectorState()
        return when (state) {
            HttpInspectorStateInterceptor.REQUEST -> {
                "**${HttpInspectorStateInterceptor.REQUEST}** ${httpInspectorEntity.httpRequestInspectorEntity?.requestMethod}"
            }

            HttpInspectorStateInterceptor.SUCCESS -> {
                "**${HttpInspectorStateInterceptor.SUCCESS}** ${httpInspectorEntity.httpResponseInspectorEntity?.responseCode} ${httpInspectorEntity.httpResponseInspectorEntity?.responseMessage}"
            }

            HttpInspectorStateInterceptor.FAILED -> {
                "**${HttpInspectorStateInterceptor.FAILED}** ${httpInspectorEntity.error}"
            }
        }
    }

    private fun createSummaryNotification() {
        // Create the summary notification
        val summaryNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Http Inspector Summary")
            .setContentText("You have new Http Inspector notifications.")
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("New message received.")
                    .addLine("Tap to view all notifications.")
                    .setSummaryText("You have new notifications."),
            )
            .setGroup(GROUP_KEY_HTTP_INSPECTOR)
            .setGroupSummary(true)
            .build()

        notificationManager.notify(SUMMARY_ID, summaryNotification)
    }
}

@Parcelize
data class HttpInspectorNotificationArgs(val id: String) : Parcelable
