package com.example.deadlinebuddy

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

enum class KittyDifficulty {
    EASY, MEDIUM, HARD
}

data class KittyAssignment(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subject: String = "",
    val description: String = "",
    val imageUri: String = "",
    val deadline: Long,
    val estimatedHours: Double = 2.0,
    val createdAt: Long = System.currentTimeMillis()
)

data class KittyTodo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val assignmentId: String = ""
)

data class KittyProfile(
    val name: String = "",
    val university: String = "",
    val department: String = "",
    val year: String = "",
    val semester: String = "",
    val imageUri: String = ""
)

class KittyDataStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("hello_kitty_deadline_buddy", Context.MODE_PRIVATE)

    fun saveAssignment(item: KittyAssignment) {
        val list = getAssignments().toMutableList()
        val index = list.indexOfFirst { it.id == item.id }

        if (index >= 0) {
            list[index] = item
        } else {
            list.add(item)
        }

        val array = JSONArray()
        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("subject", it.subject)
                    put("description", it.description)
                    put("imageUri", it.imageUri)
                    put("deadline", it.deadline)
                    put("estimatedHours", it.estimatedHours)
                    put("createdAt", it.createdAt)
                }
            )
        }

        prefs.edit().putString("assignments", array.toString()).apply()
    }

    fun getAssignments(): List<KittyAssignment> {
        val result = mutableListOf<KittyAssignment>()
        val raw = prefs.getString("assignments", "[]") ?: "[]"

        try {
            val array = JSONArray(raw)

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)

                result.add(
                    KittyAssignment(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        subject = obj.optString("subject"),
                        description = obj.optString("description"),
                        imageUri = obj.optString("imageUri"),
                        deadline = obj.optLong("deadline"),
                        estimatedHours = obj.optDouble("estimatedHours", 2.0),
                        createdAt = obj.optLong("createdAt")
                    )
                )
            }
        } catch (_: Exception) {
            return emptyList()
        }

        return result.sortedBy { it.deadline }
    }

    fun deleteAssignment(id: String) {
        val remaining = getAssignments().filterNot { it.id == id }
        val array = JSONArray()

        remaining.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("subject", it.subject)
                    put("description", it.description)
                    put("imageUri", it.imageUri)
                    put("deadline", it.deadline)
                    put("estimatedHours", it.estimatedHours)
                    put("createdAt", it.createdAt)
                }
            )
        }

        prefs.edit().putString("assignments", array.toString()).apply()

        // Remove linked to-do items too
        val remainingTodos = getTodos().filterNot {
            it.assignmentId == id
        }
        saveAllTodos(remainingTodos)
    }

    fun saveTodo(item: KittyTodo) {
        val list = getTodos().toMutableList()
        val index = list.indexOfFirst { it.id == item.id }

        if (index >= 0) {
            list[index] = item
        } else {
            list.add(item)
        }

        saveAllTodos(list)
    }

    fun getTodos(): List<KittyTodo> {
        val result = mutableListOf<KittyTodo>()
        val raw = prefs.getString("todos", "[]") ?: "[]"

        try {
            val array = JSONArray(raw)

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)

                result.add(
                    KittyTodo(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        date = obj.optLong("date"),
                        completed = obj.optBoolean("completed"),
                        assignmentId = obj.optString("assignmentId")
                    )
                )
            }
        } catch (_: Exception) {
            return emptyList()
        }

        return result.sortedBy { it.date }
    }

    private fun saveAllTodos(list: List<KittyTodo>) {
        val array = JSONArray()

        list.forEach {
            array.put(
                JSONObject().apply {
                    put("id", it.id)
                    put("title", it.title)
                    put("date", it.date)
                    put("completed", it.completed)
                    put("assignmentId", it.assignmentId)
                }
            )
        }

        prefs.edit().putString("todos", array.toString()).apply()
    }

    fun deleteTodo(id: String) {
        saveAllTodos(getTodos().filterNot { it.id == id })
    }

    fun saveProfile(profile: KittyProfile) {
        prefs.edit()
            .putString("profileName", profile.name)
            .putString("university", profile.university)
            .putString("department", profile.department)
            .putString("year", profile.year)
            .putString("semester", profile.semester)
            .putString("profileImage", profile.imageUri)
            .apply()
    }

    fun getProfile(): KittyProfile {
        return KittyProfile(
            name = prefs.getString("profileName", "") ?: "",
            university = prefs.getString("university", "") ?: "",
            department = prefs.getString("department", "") ?: "",
            year = prefs.getString("year", "") ?: "",
            semester = prefs.getString("semester", "") ?: "",
            imageUri = prefs.getString("profileImage", "") ?: ""
        )
    }
}

// Smart difficulty calculation
fun calculateKittyDifficulty(
    assignment: KittyAssignment,
    todos: List<KittyTodo>,
    now: Long = System.currentTimeMillis()
): KittyDifficulty {

    val related = todos.filter {
        it.assignmentId == assignment.id
    }

    val remaining = related.count { !it.completed }

    if (related.isNotEmpty() && remaining == 0) {
        return KittyDifficulty.EASY
    }

    val hoursLeft = (
            (assignment.deadline - now).toDouble() / 3600000.0
            ).coerceAtLeast(0.0)

    val remainingWork = if (related.isNotEmpty()) {
        remaining.toDouble()
    } else {
        assignment.estimatedHours
    }

    if (hoursLeft <= 0.0 && remainingWork > 0.0) {
        return KittyDifficulty.HARD
    }

    if (remainingWork > hoursLeft / 3.0) {
        return KittyDifficulty.HARD
    }

    if (remainingWork > hoursLeft / 8.0) {
        return KittyDifficulty.MEDIUM
    }

    return KittyDifficulty.EASY
}

fun kittyAssignmentProgress(
    assignment: KittyAssignment,
    todos: List<KittyTodo>
): Float {

    val related = todos.filter {
        it.assignmentId == assignment.id
    }

    if (related.isEmpty()) return 0f

    return related.count { it.completed }.toFloat() /
            related.size.toFloat()
}