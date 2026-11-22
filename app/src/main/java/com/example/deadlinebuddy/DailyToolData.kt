package com.example.deadlinebuddy

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

data class DailyTodoItem(
    val id: String,
    val title: String,
    val time: String = "",
    val date: String,
    val done: Boolean = false,
    val assignmentId: String = ""
)

data class DailyTodoDraft(
    val title: String,
    val date: String,
    val time: String = ""
)

data class TodoStreakStats(
    val currentStreak: Int,
    val longestStreak: Int,
    val completedDays: Int
)

object DailyTodoData {

    private const val PREFS = "DailyTodoPreferences"
    private const val TASKS_KEY = "daily_todo_tasks"
    private const val MAX_TASKS_PER_DAY = 20

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun todayKey(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun readAll(context: Context): MutableList<DailyTodoItem> {
        val result = mutableListOf<DailyTodoItem>()

        try {
            val json = prefs(context).getString(TASKS_KEY, "[]") ?: "[]"
            val array = JSONArray(json)

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)

                result.add(
                    DailyTodoItem(
                        id = item.optString("id"),
                        title = item.optString("title"),
                        time = item.optString("time"),
                        date = item.optString("date"),
                        done = item.optBoolean("done", false),
                        assignmentId = item.optString("assignmentId")
                    )
                )
            }
        } catch (_: Exception) {
        }

        return result
    }

    private fun saveAll(
        context: Context,
        tasks: List<DailyTodoItem>
    ) {
        val array = JSONArray()

        tasks.forEach { task ->
            array.put(JSONObject().apply {
                put("id", task.id)
                put("title", task.title)
                put("time", task.time)
                put("date", task.date)
                put("done", task.done)
                put("assignmentId", task.assignmentId)
            })
        }

        prefs(context).edit()
            .putString(TASKS_KEY, array.toString())
            .apply()
    }

    fun getTodayTasks(context: Context): List<DailyTodoItem> =
        getTasksForDate(context, todayKey())

    fun getTasksForDate(
        context: Context,
        date: String
    ): List<DailyTodoItem> {
        return readAll(context)
            .filter { it.date == date }
            .sortedBy { it.time }
    }

    fun getTasksForAssignment(
        context: Context,
        assignmentId: String
    ): List<DailyTodoItem> {
        return readAll(context)
            .filter { it.assignmentId == assignmentId }
            .sortedWith(compareBy<DailyTodoItem> { it.date }.thenBy { it.time })
    }

    // Manual task: adds to today's list
    fun addTask(
        context: Context,
        title: String,
        time: String = "",
        assignmentId: String = ""
    ): Boolean {
        return addTaskForDate(
            context = context,
            title = title,
            date = todayKey(),
            time = time,
            assignmentId = assignmentId
        )
    }

    // Adds a task to a selected date
    fun addTaskForDate(
        context: Context,
        title: String,
        date: String,
        time: String = "",
        assignmentId: String = ""
    ): Boolean {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty() || date.isBlank()) return false

        val allTasks = readAll(context)

        if (allTasks.count { it.date == date } >= MAX_TASKS_PER_DAY) {
            return false
        }

        allTasks.add(
            DailyTodoItem(
                id = UUID.randomUUID().toString(),
                title = cleanTitle,
                time = time,
                date = date,
                done = false,
                assignmentId = assignmentId
            )
        )

        saveAll(context, allTasks)
        return true
    }

    // Adds generated tasks and links them to their assignment.
    // Previous unfinished generated tasks for this assignment are replaced.
    // Completed tasks are preserved.
    fun addAssignmentTasks(
        context: Context,
        assignmentId: String,
        tasks: List<DailyTodoDraft>
    ): Int {
        if (assignmentId.isBlank()) return 0

        val allTasks = readAll(context)

        val completedTasks = allTasks.filter {
            it.assignmentId == assignmentId && it.done
        }

        val otherTasks = allTasks.filter {
            it.assignmentId != assignmentId
        }

        val updated = (otherTasks + completedTasks).toMutableList()
        var addedCount = 0

        tasks.forEach { draft ->
            val cleanTitle = draft.title.trim()
            if (cleanTitle.isEmpty() || draft.date.isBlank()) return@forEach

            val duplicate = updated.any {
                it.assignmentId == assignmentId &&
                        it.date == draft.date &&
                        it.title.equals(cleanTitle, ignoreCase = true)
            }

            if (duplicate) return@forEach

            val dayCount = updated.count { it.date == draft.date }
            if (dayCount >= MAX_TASKS_PER_DAY) return@forEach

            updated.add(
                DailyTodoItem(
                    id = UUID.randomUUID().toString(),
                    title = cleanTitle,
                    time = draft.time,
                    date = draft.date,
                    done = false,
                    assignmentId = assignmentId
                )
            )

            addedCount++
        }

        saveAll(context, updated)
        return addedCount
    }

    fun setTaskDone(
        context: Context,
        taskId: String,
        done: Boolean
    ) {
        val updated = readAll(context).map { task ->
            if (task.id == taskId) task.copy(done = done)
            else task
        }

        saveAll(context, updated)
    }

    fun deleteTask(
        context: Context,
        taskId: String
    ) {
        saveAll(
            context,
            readAll(context).filterNot { it.id == taskId }
        )
    }

    fun getStreakStats(context: Context): TodoStreakStats {
        val completedDates = readAll(context)
            .filter { it.done }
            .map { it.date }
            .toSet()

        if (completedDates.isEmpty()) {
            return TodoStreakStats(0, 0, 0)
        }

        val formatter = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )

        val calendar = Calendar.getInstance()

        if (todayKey() !in completedDates) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        var currentStreak = 0

        while (true) {
            val date = formatter.format(calendar.time)

            if (date in completedDates) {
                currentStreak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }

        val sortedDates = completedDates.sorted()
        var longestStreak = 0
        var runningStreak = 0
        var previousDate: String? = null

        for (date in sortedDates) {
            val currentDate = formatter.parse(date) ?: continue

            if (previousDate == null) {
                runningStreak = 1
            } else {
                val previous = formatter.parse(previousDate)

                val difference = if (previous != null) {
                    ((currentDate.time - previous.time) / 86400000L).toInt()
                } else {
                    0
                }

                runningStreak = if (difference == 1) {
                    runningStreak + 1
                } else {
                    1
                }
            }

            longestStreak = maxOf(longestStreak, runningStreak)
            previousDate = date
        }

        return TodoStreakStats(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            completedDays = completedDates.size
        )
    }
}