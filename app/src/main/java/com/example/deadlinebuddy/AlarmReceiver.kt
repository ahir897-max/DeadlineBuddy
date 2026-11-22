package com.example.deadlinebuddy

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.util.Calendar

object AlarmScheduler {

    private const val CHANNEL_ID = "deadline_buddy_clock_alarm"

    fun schedule(context: Context, alarm: BuddyAlarm) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", alarm.id)
            putExtra("alarm_label", alarm.label)
            putExtra("alarm_hour", alarm.hour)
            putExtra("alarm_minute", alarm.minute)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                !alarmManager.canScheduleExactAlarms()
            ) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancel(context: Context, alarm: BuddyAlarm) {
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun scheduleAll(context: Context) {
        AlarmData.getAlarms(context)
            .filter { it.enabled }
            .forEach { schedule(context, it) }
    }

    fun cancelAll(context: Context) {
        AlarmData.getAlarms(context).forEach {
            cancel(context, it)
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getStringExtra("alarm_id") ?: return
        val label = intent.getStringExtra("alarm_label") ?: "My Alarm"
        val hour = intent.getIntExtra("alarm_hour", 8)
        val minute = intent.getIntExtra("alarm_minute", 0)

        showAlarmNotification(context, alarmId, label, hour, minute)

        // Schedule the same alarm for the next day.
        val alarm = AlarmData.getAlarms(context)
            .firstOrNull { it.id == alarmId }

        if (alarm != null && alarm.enabled) {
            AlarmScheduler.schedule(context, alarm)
        }
    }

    private fun showAlarmNotification(
        context: Context,
        alarmId: String,
        label: String,
        hour: Int,
        minute: Int
    ) {
        val manager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "deadline_buddy_clock_alarm",
                "Clock Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Your daily Deadline Buddy alarms"
            }

            manager.createNotificationChannel(channel)
        }

        if (
            Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val timeText = String.format(
            java.util.Locale.getDefault(),
            "%02d:%02d",
            hour,
            minute
        )

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, "deadline_buddy_clock_alarm")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("⏰ $label")
                .setContentText("It's $timeText — time for your reminder! ♡")
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_ALARM)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("⏰ $label")
                .setContentText("It's $timeText — time for your reminder! ♡")
                .setAutoCancel(true)
                .build()
        }

        manager.notify(alarmId.hashCode(), notification)
    }
}