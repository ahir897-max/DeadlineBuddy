package com.example.deadlinebuddy

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

private val DailyPink = Color(0xFFFFB6D0)
private val DailyDarkPink = Color(0xFFE85D91)
private val DailyBackground = Color(0xFFFFF5F8)
private val DailyText = Color(0xFF694456)
private val DailyPurple = Color(0xFFE8DDFB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTodoScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var tasks by remember {
        mutableStateOf(DailyTodoData.getTodayTasks(context))
    }

    var title by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var refresh by remember { mutableIntStateOf(0) }

    val stats = remember(refresh) {
        DailyTodoData.getStreakStats(context)
    }

    val completedCount = tasks.count { it.done }
    val progress = if (tasks.isEmpty()) 0f
    else completedCount.toFloat() / tasks.size

    fun reload() {
        tasks = DailyTodoData.getTodayTasks(context)
        refresh++
    }

    Scaffold(
        containerColor = DailyBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Daily To-do 🎀",
                        color = DailyText,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = DailyDarkPink
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DailyBackground
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE2EE)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎀  MY CUTE STREAK  🎀", color = DailyDarkPink,
                            fontWeight = FontWeight.Bold)

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "🔥 ${stats.currentStreak} Days",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DailyDarkPink
                        )

                        Text(
                            "You're doing amazing, cutie! 💗",
                            color = DailyText
                        )

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🏆")
                                Text("Longest", color = DailyText, fontSize = 12.sp)
                                Text("${stats.longestStreak} days",
                                    color = DailyDarkPink,
                                    fontWeight = FontWeight.Bold)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("💖")
                                Text("Completed days",
                                    color = DailyText,
                                    fontSize = 12.sp)
                                Text("${stats.completedDays}",
                                    color = DailyDarkPink,
                                    fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Complete at least 1 task every day to keep your streak!",
                            color = DailyText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Today's Progress 💗",
                            color = DailyText,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(9.dp),
                            color = DailyDarkPink,
                            trackColor = DailyPink
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            "$completedCount / ${tasks.size} tasks completed",
                            color = DailyText,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            item {
                Text(
                    "Add a new task ✨",
                    color = DailyText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("What do you need to do?") },
                            placeholder = { Text("Example: Morning brush 🪥") },
                            singleLine = true
                        )

                        OutlinedButton(
                            onClick = {
                                val calendar = Calendar.getInstance()
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        val amPm = if (hour < 12) "AM" else "PM"
                                        val displayHour =
                                            when (hour % 12) {
                                                0 -> 12
                                                else -> hour % 12
                                            }
                                        selectedTime =
                                            String.format(
                                                "%02d:%02d %s",
                                                displayHour,
                                                minute,
                                                amPm
                                            )
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    false
                                ).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (selectedTime.isBlank())
                                    "＋ Add time (optional)"
                                else "🕒 $selectedTime"
                            )
                        }

                        Button(
                            onClick = {
                                val added = DailyTodoData.addTask(
                                    context = context,
                                    title = title,
                                    time = selectedTime
                                )

                                if (added) {
                                    title = ""
                                    selectedTime = ""
                                    reload()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = title.isNotBlank() && tasks.size < 20,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DailyDarkPink
                            )
                        ) {
                            Text("Add to my list 💗")
                        }

                        Text(
                            "${tasks.size}/20 tasks added today",
                            color = DailyText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Text(
                    "My Tasks 🎀",
                    color = DailyText,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (tasks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(25.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🐱", fontSize = 42.sp)
                            Text(
                                "Your list is waiting!",
                                color = DailyText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Add your first task above 💕",
                                color = DailyText
                            )
                        }
                    }
                }
            }

            items(tasks, key = { it.id }) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (task.done)
                            Color(0xFFFFEAF2) else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.done,
                            onCheckedChange = { checked ->
                                DailyTodoData.setTaskDone(
                                    context,
                                    task.id,
                                    checked
                                )
                                reload()
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DailyDarkPink
                            )
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                task.title,
                                color = DailyText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )

                            if (task.time.isNotBlank()) {
                                Text(
                                    "🕒 ${task.time}",
                                    color = DailyDarkPink,
                                    fontSize = 12.sp
                                )
                            }

                            if (task.assignmentId.isNotBlank()) {
                                Text(
                                    "📚 Assignment task",
                                    color = DailyDarkPink,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                DailyTodoData.deleteTask(context, task.id)
                                reload()
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete task",
                                tint = DailyDarkPink
                            )
                        }
                    }
                }
            }

            if (completedCount > 0) {
                item {
                    Text(
                        "Yay! You completed $completedCount task(s)! 🎉",
                        color = DailyDarkPink,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}