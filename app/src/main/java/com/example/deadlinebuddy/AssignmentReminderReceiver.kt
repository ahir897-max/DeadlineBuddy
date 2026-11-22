package com.example.deadlinebuddy

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class AssignmentReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "deadline_buddy_kitty_channel"
        const val CHANNEL_NAME = "Kitty Assignment Reminders"
    }

    override fun onReceive(context: Context, intent: Intent) {

        createNotificationChannel(context)

        // Android 13+ notification permission check
        if (
            Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val assignmentTitle =
            intent.getStringExtra("assignment_title")
                ?: "My Assignment"

        val message =
            intent.getStringExtra("reminder_message")
                ?: "Your assignment deadline is approaching!"

        val notificationId =
            intent.getIntExtra("notification_id", 1001)

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("🎀 $assignmentTitle")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🐱 $message\n\nDon't forget your assignment! 💗")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        try {
            NotificationManagerCompat
                .from(context)
                .notify(notificationId, notification)

        } catch (_: SecurityException) {
            // Notification permission is not available
        }
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Cute Hello Kitty assignment deadline reminders 🎀"
                enableVibration(true)
            }

            val manager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }
}