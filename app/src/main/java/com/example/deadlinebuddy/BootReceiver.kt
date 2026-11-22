package com.example.deadlinebuddy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Locale

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val assignments = BuddyData.getAssignments(context)

        assignments.forEach { assignment ->
            val formats = listOf(
                "dd/MM/yyyy HH:mm",
                "dd/MM/yyyy hh:mm a",
                "yyyy-MM-dd HH:mm"
            )

            val deadline = formats.firstNotNullOfOrNull { pattern ->
                try {
                    SimpleDateFormat(pattern, Locale.getDefault())
                        .apply { isLenient = false }
                        .parse(assignment.deadline)
                } catch (_: Exception) {
                    null
                }
            }

            deadline?.let {
                ReminderScheduler.schedule(
                    context,
                    assignment.title,
                    it.time
                )
            }
        }
    }
}