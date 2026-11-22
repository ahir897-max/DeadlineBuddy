package com.example.deadlinebuddy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@Composable
fun AddTaskScreen(
    context: Context,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var selectedDate by remember {
        mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time))
    }
    var selectedTime by remember { mutableStateOf("09:00 AM") }
    var message by remember { mutableStateOf("") }
    var refresh by remember { mutableIntStateOf(0) }

    val pink = Color(0xFFFFF0F6)
    val darkPink = Color(0xFFE85D8E)
    val textColor = Color(0xFF553746)

    val tasks = remember(refresh) { BuddyData.getTasks(context) }
    val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
    val todayCount = tasks.count { it.date == today }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pink)
            .padding(20.dp)
    ) {
        Text(
            "♡ Add New Task",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = darkPink
        )

        Text(
            "Plan your day, one little step at a time!",
            color = textColor,
            modifier = Modifier.padding(top = 5.dp, bottom = 16.dp)
        )

        Text("Today's Tasks: $todayCount / 10", color = darkPink, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title") },
            placeholder = { Text("e.g. Study Mathematics") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Write your task details") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            minLines = 2
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = hours,
            onValueChange = { hours = it.filter { ch -> ch.isDigit() || ch == '.' } },
            label = { Text("Estimated Hours") },
            placeholder = { Text("e.g. 2") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                val c = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        selectedDate = "%02d/%02d/%04d".format(day, month + 1, year)
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📅 Date: $selectedDate")
        }

        OutlinedButton(
            onClick = {
                val c = Calendar.getInstance()
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        selectedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
                    },
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    false
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⏰ Time: $selectedTime")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                val estimated = hours.toDoubleOrNull()
                when {
                    title.isBlank() -> message = "Please enter a task title ♡"
                    estimated == null || estimated <= 0 -> message = "Enter valid estimated hours"
                    selectedDate == today && todayCount >= 10 ->
                        message = "You can add maximum 10 tasks today!"
                    else -> {
                        val newTask = BuddyTask(
                            id = UUID.randomUUID().toString(),
                            title = title.trim(),
                            description = description.trim(),
                            estimatedHours = estimated,
                            date = selectedDate,
                            time = selectedTime,
                            done = false
                        )
                        BuddyData.saveTasks(context, tasks + newTask)
                        title = ""
                        description = ""
                        hours = ""
                        message = "Task saved successfully! ♡"
                        refresh++
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = darkPink),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Save Task ♡", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }

        if (message.isNotBlank()) {
            Text(message, color = darkPink, modifier = Modifier.padding(8.dp))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        Text("♡ My To-Do List", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = textColor)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.done,
                            onCheckedChange = { checked ->
                                val updated = tasks.map {
                                    if (it.id == task.id) it.copy(done = checked) else it
                                }
                                BuddyData.saveTasks(context, updated)
                                refresh++
                            },
                            colors = CheckboxDefaults.colors(checkedColor = darkPink)
                        )
                        Column(Modifier.weight(1f)) {
                            Text(
                                task.title,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text("${task.date} • ${task.time} • ${task.estimatedHours} hours", fontSize = 12.sp)
                            if (task.description.isNotBlank()) {
                                Text(task.description, fontSize = 13.sp)
                            }
                        }
                        Text(
                            if (task.done) "Done ♡" else "To-Do",
                            color = if (task.done) Color(0xFF65A88A) else darkPink,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("← Back to Dashboard", color = darkPink)
        }
    }
}