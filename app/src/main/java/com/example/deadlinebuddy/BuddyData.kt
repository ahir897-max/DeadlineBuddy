package com.example.deadlinebuddy


import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class BuddyTask(
    val id: String,
    val title: String,
    val description: String,
    val estimatedHours: Double,
    val date: String,
    val time: String,
    val done: Boolean = false
)

data class BuddyAssignment(
    val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val deadline: String,
    val estimatedHours: Double,
    val imageUri: String = "",
    val done: Boolean = false
)

object BuddyData {

    private const val FILE = "DeadlineBuddyData"

    private fun prefs(context: Context) =
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun getTasks(context: Context): List<BuddyTask> {
        val array = JSONArray(
            prefs(context).getString("task_list", "[]") ?: "[]"
        )
        return (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            BuddyTask(
                id = o.optString("id"),
                title = o.optString("title"),
                description = o.optString("description"),
                estimatedHours = o.optDouble("hours"),
                date = o.optString("date"),
                time = o.optString("time"),
                done = o.optBoolean("done")
            )
        }
    }

    fun saveTasks(context: Context, tasks: List<BuddyTask>) {
        val array = JSONArray()
        tasks.forEach { task ->
            array.put(JSONObject().apply {
                put("id", task.id)
                put("title", task.title)
                put("description", task.description)
                put("hours", task.estimatedHours)
                put("date", task.date)
                put("time", task.time)
                put("done", task.done)
            })
        }
        prefs(context).edit().putString("task_list", array.toString()).apply()
    }

    fun getAssignments(context: Context): List<BuddyAssignment> {
        val array = JSONArray(
            prefs(context).getString("assignment_list", "[]") ?: "[]"
        )
        return (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            BuddyAssignment(
                id = o.optString("id"),
                title = o.optString("title"),
                subject = o.optString("subject"),
                description = o.optString("description"),
                deadline = o.optString("deadline"),
                estimatedHours = o.optDouble("hours"),
                imageUri = o.optString("imageUri"),
                done = o.optBoolean("done")
            )
        }
    }

    fun saveAssignments(
        context: Context,
        assignments: List<BuddyAssignment>
    ) {
        val array = JSONArray()
        assignments.forEach { item ->
            array.put(JSONObject().apply {
                put("id", item.id)
                put("title", item.title)
                put("subject", item.subject)
                put("description", item.description)
                put("deadline", item.deadline)
                put("hours", item.estimatedHours)
                put("imageUri", item.imageUri)
                put("done", item.done)
            })
        }
        prefs(context).edit()
            .putString("assignment_list", array.toString())
            .apply()
    }
}