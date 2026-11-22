package com.example.deadlinebuddy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object ReminderScheduler {

    private const val ONE_DAY = 24 * 60 * 60 * 1000L
    private const val ONE_HOUR = 60 * 60 * 1000L

    fun schedule(
        context: Context,
        title: String,
        deadlineMillis: Long
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val baseId = (title + deadlineMillis).hashCode()
        val now = System.currentTimeMillis()

        val reminders = listOf(
            Pair(
                deadlineMillis - ONE_DAY,
                "Your assignment is due tomorrow! 🎀"
            ),
            Pair(
                deadlineMillis - ONE_HOUR,
                "Your assignment is due in 1 hour! 💗"
            )
        )

        reminders.forEachIndexed { index, reminder ->
            val requestCode = baseId + index

            val intent = Intent(
                context,
                AssignmentReminderReceiver::class.java
            ).apply {
                putExtra("assignment_title", title)
                putExtra("reminder_message", reminder.second)
                putExtra("notification_id", requestCode)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            if (reminder.first > now) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminder.first,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        reminder.first,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    fun cancel(
        context: Context,
        title: String,
        deadlineMillis: Long
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val baseId = (title + deadlineMillis).hashCode()

        repeat(2) { index ->
            val requestCode = baseId + index

            val intent = Intent(
                context,
                AssignmentReminderReceiver::class.java
            )

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}