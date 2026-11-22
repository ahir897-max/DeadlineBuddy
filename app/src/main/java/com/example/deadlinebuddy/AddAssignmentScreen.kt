package com.example.deadlinebuddy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

private val AssignmentPink = Color(0xFFF58EAF)
private val AssignmentDarkPink = Color(0xFFB94E78)
private val AssignmentBackground = Color(0xFFFFF8F7)
private val AssignmentSoftPink = Color(0xFFFFEAF1)

@Composable
fun AddAssignmentScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var estimatedHours by remember { mutableStateOf("2") }
    var deadline by remember { mutableStateOf("") }
    var selectedImagePath by remember { mutableStateOf("") }

    var imageBitmap by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val savedPath = copyImageToAppStorage(context, uri)

                if (savedPath != null) {
                    selectedImagePath = savedPath
                    imageBitmap = BitmapFactory.decodeFile(savedPath)
                } else {
                    Toast.makeText(
                        context,
                        "Could not save image. Try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "Image selection failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val calendar = remember { Calendar.getInstance() }

    fun showDateTimePicker() {
        val now = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)

                        deadline = SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                        ).format(calendar.time)
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
                ).show()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AssignmentBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text(
                "← Back",
                color = AssignmentDarkPink,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Add New Assignment 🎀",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AssignmentDarkPink
        )

        Text(
            "Plan your work and stay on track ♡",
            fontSize = 15.sp,
            color = Color(0xFF96717F)
        )

        Spacer(modifier = Modifier.height(22.dp))

        AssignmentInput(
            label = "Assignment Title",
            value = title,
            onValueChange = { title = it }
        )

        AssignmentInput(
            label = "Subject",
            value = subject,
            onValueChange = { subject = it }
        )

        AssignmentInput(
            label = "Description",
            value = description,
            onValueChange = { description = it },
            singleLine = false
        )

        AssignmentInput(
            label = "Estimated Work Hours",
            value = estimatedHours,
            onValueChange = { estimatedHours = it },
            numeric = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDateTimePicker() },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = AssignmentSoftPink
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "📅 Assignment Deadline",
                    fontWeight = FontWeight.Bold,
                    color = AssignmentDarkPink
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    if (deadline.isBlank()) {
                        "Tap here to select date and time"
                    } else {
                        deadline
                    },
                    color = Color(0xFF704A59),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "Assignment Picture 🖼️",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AssignmentDarkPink
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = "Assignment picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                contentScale = ContentScale.Crop
            )

            TextButton(
                onClick = {
                    selectedImagePath = ""
                    imageBitmap = null
                }
            ) {
                Text(
                    "Remove Picture",
                    color = AssignmentDarkPink
                )
            }
        }

        OutlinedButton(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(18.dp)
        ) {
            Text(
                if (imageBitmap == null) {
                    "＋ Choose Picture from Gallery"
                } else {
                    "Change Picture"
                },
                color = AssignmentDarkPink
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                val hours = estimatedHours.toDoubleOrNull()

                when {
                    title.isBlank() -> {
                        Toast.makeText(
                            context,
                            "Please enter assignment title.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    subject.isBlank() -> {
                        Toast.makeText(
                            context,
                            "Please enter subject.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    deadline.isBlank() -> {
                        Toast.makeText(
                            context,
                            "Please select deadline.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    hours == null || hours <= 0 -> {
                        Toast.makeText(
                            context,
                            "Enter valid estimated hours.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        val assignments =
                            BuddyData.getAssignments(context).toMutableList()

                        val newAssignment = BuddyAssignment(
                            id = UUID.randomUUID().toString(),
                            title = title.trim(),
                            subject = subject.trim(),
                            description = description.trim(),
                            deadline = deadline,
                            estimatedHours = hours,
                            imageUri = selectedImagePath,
                            done = false
                        )

                        assignments.add(newAssignment)

                        BuddyData.saveAssignments(
                            context,
                            assignments
                        )

                        // Keep the existing assignment counter in sync.
                        val newCount = assignments.size

                        context.getSharedPreferences(
                            "DeadlineBuddyPrefs",
                            Context.MODE_PRIVATE
                        ).edit()
                            .putString(
                                "assignments",
                                assignments.joinToString("\n") {
                                    "${it.title} | ${it.subject} | ${it.deadline} | ${it.description}"
                                }
                            )
                            .putInt("assignment_count", newCount)
                            .apply()

                        context.getSharedPreferences(
                            "DeadlineBuddyProfile",
                            Context.MODE_PRIVATE
                        ).edit()
                            .putInt("assignmentCount", newCount)
                            .apply()

                        val deadlineDate = try {
                            SimpleDateFormat(
                                "dd/MM/yyyy HH:mm",
                                Locale.getDefault()
                            ).parse(deadline)
                        } catch (_: Exception) {
                            null
                        }

                        deadlineDate?.let {
                            ReminderScheduler.schedule(
                                context,
                                title.trim(),
                                it.time
                            )
                        }

                        Toast.makeText(
                            context,
                            "Assignment saved successfully! 🎀",
                            Toast.LENGTH_LONG
                        ).show()

                        onBackClick()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AssignmentPink
            )
        ) {
            Text(
                "Save Assignment ♡",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun AssignmentInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    numeric: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        shape = RoundedCornerShape(16.dp)
    )
}

private fun copyImageToAppStorage(
    context: Context,
    uri: Uri
): String? {
    val imageFolder = File(
        context.filesDir,
        "assignment_images"
    )

    if (!imageFolder.exists()) {
        imageFolder.mkdirs()
    }

    val imageFile = File(
        imageFolder,
        "assignment_${UUID.randomUUID()}.jpg"
    )

    return try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            imageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        if (imageFile.exists() && imageFile.length() > 0) {
            imageFile.absolutePath
        } else {
            null
        }
    } catch (_: Exception) {
        imageFile.delete()
        null
    }
}