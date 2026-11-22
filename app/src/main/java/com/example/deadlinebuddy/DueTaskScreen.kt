package com.example.deadlinebuddy

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DuePink = Color(0xFFF58EAF)
private val DueDarkPink = Color(0xFFB94E78)
private val DueBackground = Color(0xFFFFF8F7)
private val DueSoftPink = Color(0xFFFFEAF1)

@Composable
fun DueTasksScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var refresh by remember { mutableIntStateOf(0) }

    val assignments = remember(refresh) {
        BuddyData.getAssignments(context)
    }

    val tasks = remember(refresh) {
        BuddyData.getTasks(context)
    }

    val unfinishedAssignments = assignments.filter { !it.done }
    val completedAssignments = assignments.filter { it.done }

    val unfinishedTasks = tasks.filter { !it.done }
    val completedTasks = tasks.filter { it.done }

    val today = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DueBackground)
            .padding(18.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back", color = DueDarkPink)
        }

        Text(
            "Due Tasks ♡",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DueDarkPink
        )

        Text(
            "Everything you still need to finish 🎀",
            color = Color(0xFF96717F)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryCard(
                title = "Assignments",
                count = unfinishedAssignments.size,
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = "Daily Tasks",
                count = unfinishedTasks.size,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                SectionHeading("📚 Unfinished Assignments")
            }

            if (unfinishedAssignments.isEmpty()) {
                item {
                    EmptyMessage("No unfinished assignments! 🌷")
                }
            }

            items(
                unfinishedAssignments,
                key = { "assignment_${it.id}" }
            ) { assignment ->
                val dueDate = assignment.deadline
                    .take(10)

                val isOverdue = isDeadlineOverdue(
                    assignment.deadline
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = { checked ->
                                val updated = BuddyData
                                    .getAssignments(context)
                                    .map {
                                        if (it.id == assignment.id) {
                                            it.copy(done = checked)
                                        } else {
                                            it
                                        }
                                    }

                                BuddyData.saveAssignments(
                                    context,
                                    updated
                                )
                                refresh++
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DuePink
                            )
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                assignment.title,
                                fontWeight = FontWeight.Bold,
                                color = DueDarkPink,
                                fontSize = 16.sp
                            )

                            Text(
                                "📖 ${assignment.subject}",
                                color = Color(0xFF806A73),
                                fontSize = 13.sp
                            )

                            Text(
                                "⏰ ${assignment.deadline}",
                                color = Color(0xFF806A73),
                                fontSize = 12.sp
                            )

                            Text(
                                if (isOverdue) {
                                    "Overdue • Please finish it!"
                                } else {
                                    "Pending • Due date: $dueDate"
                                },
                                color = if (isOverdue) {
                                    Color(0xFFD34B61)
                                } else {
                                    DueDarkPink
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeading("🌸 Daily To-do Tasks")
            }

            if (unfinishedTasks.isEmpty()) {
                item {
                    EmptyMessage("No unfinished daily tasks! 💗")
                }
            }

            items(
                unfinishedTasks,
                key = { "task_${it.id}" }
            ) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DueSoftPink
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = false,
                            onCheckedChange = { checked ->
                                val updated = BuddyData
                                    .getTasks(context)
                                    .map {
                                        if (it.id == task.id) {
                                            it.copy(done = checked)
                                        } else {
                                            it
                                        }
                                    }

                                BuddyData.saveTasks(
                                    context,
                                    updated
                                )
                                refresh++
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DuePink
                            )
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                task.title,
                                fontWeight = FontWeight.Bold,
                                color = DueDarkPink
                            )

                            if (task.description.isNotBlank()) {
                                Text(
                                    task.description,
                                    color = Color(0xFF806A73),
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                "📅 ${task.date} • ${task.time}",
                                color = Color(0xFF806A73),
                                fontSize = 12.sp
                            )

                            Text(
                                if (task.date == today) {
                                    "Today's task ♡"
                                } else {
                                    "Daily task"
                                },
                                color = DueDarkPink,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeading("✓ Completed")
            }

            if (
                completedAssignments.isEmpty() &&
                completedTasks.isEmpty()
            ) {
                item {
                    EmptyMessage("Your completed tasks will appear here.")
                }
            }

            items(
                completedAssignments,
                key = { "completed_assignment_${it.id}" }
            ) { assignment ->
                CompletedItem(
                    title = assignment.title,
                    subtitle = "Assignment • ${assignment.deadline}",
                    onUndo = {
                        val updated = BuddyData
                            .getAssignments(context)
                            .map {
                                if (it.id == assignment.id) {
                                    it.copy(done = false)
                                } else {
                                    it
                                }
                            }

                        BuddyData.saveAssignments(context, updated)
                        refresh++
                    }
                )
            }

            items(
                completedTasks,
                key = { "completed_task_${it.id}" }
            ) { task ->
                CompletedItem(
                    title = task.title,
                    subtitle = "Daily task • ${task.date}",
                    onUndo = {
                        val updated = BuddyData
                            .getTasks(context)
                            .map {
                                if (it.id == task.id) {
                                    it.copy(done = false)
                                } else {
                                    it
                                }
                            }

                        BuddyData.saveTasks(context, updated)
                        refresh++
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = DueSoftPink
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                "$count",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = DueDarkPink
            )

            Text(
                title,
                fontSize = 12.sp,
                color = Color(0xFF806A73)
            )
        }
    }
}

@Composable
private fun SectionHeading(title: String) {
    Text(
        title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = DueDarkPink,
        modifier = Modifier.padding(vertical = 5.dp)
    )
}

@Composable
private fun EmptyMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            message,
            modifier = Modifier.padding(18.dp),
            color = Color(0xFF96717F)
        )
    }
}

@Composable
private fun CompletedItem(
    title: String,
    subtitle: String,
    onUndo: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF2F8F1)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("✓", color = Color(0xFF4C9B71), fontSize = 22.sp)

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF527A5D)
                )
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF78917C)
                )
            }

            TextButton(onClick = onUndo) {
                Text("Undo", color = DueDarkPink)
            }
        }
    }
}

private fun isDeadlineOverdue(deadline: String): Boolean {
    val formats = listOf(
        "dd/MM/yyyy HH:mm",
        "dd/MM/yyyy hh:mm a",
        "yyyy-MM-dd HH:mm"
    )

    for (format in formats) {
        try {
            val date = SimpleDateFormat(
                format,
                Locale.getDefault()
            ).apply {
                isLenient = false
            }.parse(deadline)

            if (date != null) {
                return date.time < System.currentTimeMillis()
            }
        } catch (_: Exception) {
        }
    }

    return false
}