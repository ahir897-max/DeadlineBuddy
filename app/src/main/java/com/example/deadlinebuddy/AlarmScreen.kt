package com.example.deadlinebuddy

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlarmScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    var alarms by remember {
        mutableStateOf(AlarmData.getAlarms(context))
    }

    var alarmLabel by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf(8) }
    var selectedMinute by remember { mutableStateOf(0) }
    var editingAlarmId by remember { mutableStateOf<String?>(null) }

    val exactAlarmLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager =
                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                if (manager.canScheduleExactAlarms()) {
                    AlarmScheduler.scheduleAll(context)
                }
            }
            alarms = AlarmData.getAlarms(context)
        }

    LaunchedEffect(Unit) {
        AlarmScheduler.scheduleAll(context)
    }

    fun refreshAlarms() {
        alarms = AlarmData.getAlarms(context)
    }

    fun openTimePicker() {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
            },
            selectedHour,
            selectedMinute,
            false
        ).show()
    }

    fun saveAlarm() {
        val label = alarmLabel.trim().ifEmpty { "My Alarm" }

        if (editingAlarmId == null) {
            val newAlarm = AlarmData.addAlarm(
                context,
                selectedHour,
                selectedMinute,
                label
            )
            AlarmScheduler.schedule(context, newAlarm)
        } else {
            val oldAlarm = alarms.firstOrNull {
                it.id == editingAlarmId
            }

            if (oldAlarm != null) {
                AlarmScheduler.cancel(context, oldAlarm)

                val updatedAlarm = oldAlarm.copy(
                    hour = selectedHour,
                    minute = selectedMinute,
                    label = label,
                    enabled = true
                )

                AlarmData.updateAlarm(context, updatedAlarm)
                AlarmScheduler.schedule(context, updatedAlarm)
            }
        }

        alarmLabel = ""
        editingAlarmId = null
        refreshAlarms()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEAF3))
            .padding(16.dp)
    ) {

        TextButton(onClick = onBack) {
            Text(
                "← Back",
                color = Color(0xFFD96FA5),
                fontStyle = FontStyle.Italic
            )
        }

        Text(
            "⏰ My Cute Alarms 🎀",
            fontSize = 25.sp,
            color = Color(0xFFD96FA5),
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Set your daily reminders ♡",
            color = Color(0xFF806C99),
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF4F9)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    if (editingAlarmId == null) "Add a New Alarm 🎀"
                    else "Edit Your Alarm ✨",
                    color = Color(0xFFD96FA5),
                    fontSize = 19.sp
                )

                OutlinedTextField(
                    value = alarmLabel,
                    onValueChange = { alarmLabel = it },
                    label = { Text("Alarm name") },
                    placeholder = { Text("e.g. Wake up, Study time") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = { openTimePicker() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9B83CC)
                    )
                ) {
                    Text(
                        String.format(
                            java.util.Locale.getDefault(),
                            "Choose Time: %02d:%02d",
                            selectedHour,
                            selectedMinute
                        )
                    )
                }

                Button(
                    onClick = { saveAlarm() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD96FA5)
                    )
                ) {
                    Text(
                        if (editingAlarmId == null) "＋ Save Alarm"
                        else "Save Changes"
                    )
                }

                if (editingAlarmId != null) {
                    TextButton(
                        onClick = {
                            editingAlarmId = null
                            alarmLabel = ""
                            selectedHour = 8
                            selectedMinute = 0
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancel Edit")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !(context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                .canScheduleExactAlarms()
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEDE4FA)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Allow exact alarm access for more accurate timing.",
                        color = Color(0xFF493746)
                    )

                    TextButton(
                        onClick = {
                            val intent = Intent(
                                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                Uri.parse("package:${context.packageName}")
                            )
                            exactAlarmLauncher.launch(intent)
                        }
                    ) {
                        Text("Allow Alarm Permission")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            "Your Saved Alarms ♡",
            fontSize = 20.sp,
            color = Color(0xFF9B83CC)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (alarms.isEmpty()) {
            Text(
                "No alarms yet! Add your first alarm above 🌸",
                color = Color(0xFF806C99)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(alarms, key = { it.id }) { alarm ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF4F9)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        String.format(
                                            java.util.Locale.getDefault(),
                                            "%02d:%02d",
                                            alarm.hour,
                                            alarm.minute
                                        ),
                                        fontSize = 29.sp,
                                        color = Color(0xFFD96FA5)
                                    )

                                    Text(
                                        alarm.label,
                                        color = Color(0xFF493746)
                                    )

                                    Text(
                                        "Repeats every day",
                                        fontSize = 12.sp,
                                        color = Color(0xFF806C99)
                                    )
                                }

                                Switch(
                                    checked = alarm.enabled,
                                    onCheckedChange = { enabled ->
                                        val updated = alarm.copy(
                                            enabled = enabled
                                        )

                                        AlarmData.updateAlarm(
                                            context,
                                            updated
                                        )

                                        if (enabled) {
                                            AlarmScheduler.schedule(
                                                context,
                                                updated
                                            )
                                        } else {
                                            AlarmScheduler.cancel(
                                                context,
                                                alarm
                                            )
                                        }

                                        refreshAlarms()
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        editingAlarmId = alarm.id
                                        alarmLabel = alarm.label
                                        selectedHour = alarm.hour
                                        selectedMinute = alarm.minute
                                    }
                                ) {
                                    Text("Edit")
                                }

                                TextButton(
                                    onClick = {
                                        AlarmScheduler.cancel(
                                            context,
                                            alarm
                                        )
                                        AlarmData.deleteAlarm(
                                            context,
                                            alarm.id
                                        )
                                        refreshAlarms()
                                    }
                                ) {
                                    Text(
                                        "Delete",
                                        color = Color(0xFFD96FA5)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}