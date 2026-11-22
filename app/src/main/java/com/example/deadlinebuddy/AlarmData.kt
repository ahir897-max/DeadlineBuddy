package com.example.deadlinebuddy

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class BuddyAlarm(
    val id: String,
    val hour: Int,
    val minute: Int,
    val label: String,
    val enabled: Boolean = true
)

object AlarmData {

    private const val PREFS = "DeadlineBuddyAlarms"
    private const val KEY = "alarm_list"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getAlarms(context: Context): List<BuddyAlarm> {
        val array = JSONArray(
            prefs(context).getString(KEY, "[]") ?: "[]"
        )

        return (0 until array.length()).map { index ->
            val item = array.getJSONObject(index)

            BuddyAlarm(
                id = item.optString("id"),
                hour = item.optInt("hour"),
                minute = item.optInt("minute"),
                label = item.optString("label", "My Alarm"),
                enabled = item.optBoolean("enabled", true)
            )
        }.sortedWith(compareBy({ it.hour }, { it.minute }))
    }

    fun saveAlarms(
        context: Context,
        alarms: List<BuddyAlarm>
    ) {
        val array = JSONArray()

        alarms.forEach { alarm ->
            val item = JSONObject().apply {
                put("id", alarm.id)
                put("hour", alarm.hour)
                put("minute", alarm.minute)
                put("label", alarm.label)
                put("enabled", alarm.enabled)
            }
            array.put(item)
        }

        prefs(context).edit()
            .putString(KEY, array.toString())
            .apply()
    }

    fun addAlarm(
        context: Context,
        hour: Int,
        minute: Int,
        label: String
    ): BuddyAlarm {
        val alarm = BuddyAlarm(
            id = UUID.randomUUID().toString(),
            hour = hour,
            minute = minute,
            label = label.ifBlank { "My Alarm" },
            enabled = true
        )

        val alarms = getAlarms(context).toMutableList()
        alarms.add(alarm)
        saveAlarms(context, alarms)

        return alarm
    }

    fun updateAlarm(
        context: Context,
        updatedAlarm: BuddyAlarm
    ) {
        val alarms = getAlarms(context).map { alarm ->
            if (alarm.id == updatedAlarm.id) updatedAlarm else alarm
        }

        saveAlarms(context, alarms)
    }

    fun deleteAlarm(
        context: Context,
        alarmId: String
    ) {
        val alarms = getAlarms(context)
            .filterNot { it.id == alarmId }

        saveAlarms(context, alarms)
    }
}