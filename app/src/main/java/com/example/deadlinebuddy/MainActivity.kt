package com.example.deadlinebuddy

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DeadlineBuddyApp(this)
        }
    }
}

@Composable
fun DeadlineBuddyApp(context: Context) {

    val prefs = remember {
        context.getSharedPreferences(
            "DeadlineBuddyProfile",
            Context.MODE_PRIVATE
        )
    }

    var screen by remember { mutableStateOf("home") }

    var userName by remember {
        mutableStateOf(
            prefs.getString("name", "Student") ?: "Student"
        )
    }

    var refresh by remember { mutableStateOf(0) }

    MaterialTheme {

        when (screen) {

            "home" -> {
                HomeScreen(
                    onLogin = { screen = "login" },
                    onSignUp = { screen = "signup" }
                )
            }

            "login" -> {
                LoginScreen(
                    savedName = prefs.getString("name", "") ?: "",
                    savedPassword = prefs.getString("password", "") ?: "",
                    onLoginSuccess = {
                        userName =
                            prefs.getString("name", "Student")
                                ?: "Student"
                        screen = "dashboard"
                    },
                    onBack = {
                        screen = "home"
                    }
                )
            }

            "signup" -> {
                SignUpScreen(
                    onAccountCreated = {
                        userName =
                            prefs.getString("name", "Student")
                                ?: "Student"
                        screen = "dashboard"
                    },
                    onBack = {
                        screen = "home"
                    }
                )
            }

            "dashboard" -> {
                CuteDashboardScreen(
                    userName = userName,
                    onAddTask = {
                        screen = "addTask"
                    },
                    onAddAssignment = {
                        screen = "assignment"
                    },
                    onCalendar = {
                        screen = "calendar"
                    },
                    onDueAssignments = {
                        screen = "dueAssignments"
                    },
                    onReminder = {
                        screen = "reminder"
                    },
                    onAutoTaskBreakdown = {
                        screen = "autoTask"
                    },
                    onProfile = {
                        screen = "profile"
                    }
                )
            }

            "profile" -> {
                ProfileScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    },
                    onSaved = {
                        userName =
                            prefs.getString("name", "Student")
                                ?: "Student"
                        refresh++
                    }
                )
            }

            "addTask" -> {
                AddTaskScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    }
                )
            }

            "assignment" -> {
                AddAssignmentScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    }
                )
            }

            "reminder" -> {
                ReminderScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    }
                )
            }

            "calendar" -> {
                CalendarScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    }
                )
            }

            "dueAssignments" -> {
                DueAssignmentsScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    }
                )
            }

            "autoTask" -> {
                AutoBreakdownScreen(
                    prefs = prefs,
                    onBack = {
                        screen = "dashboard"
                    }
                )
            }
        }
    }
}


// ======================================================
// HOME
// ======================================================

@Composable
fun HomeScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F7))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "Deadline Buddy ♥",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5795C8)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(
                id = R.drawable.hello_kitty_mew
            ),
            contentDescription = "Hello Kitty",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(30.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your little study buddy ♡",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF704A59)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Let's make your deadlines stress-free 🌸",
            fontSize = 16.sp,
            color = Color(0xFF8A6572)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = onLogin,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(35.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text(
                text = "Log In →",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onSignUp,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(35.dp)
        ) {
            Text(
                text = "Sign Up ♡",
                fontSize = 20.sp,
                color = Color(0xFFD05E8A)
            )
        }
    }
}


// ======================================================
// LOGIN
// ======================================================

@Composable
fun LoginScreen(
    savedName: String,
    savedPassword: String,
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit
) {

    var name by remember { mutableStateOf(savedName) }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    SimplePage {

        Text(
            text = "Welcome Back ♡",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                error = ""
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = ""
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation =
                if (showPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (showPassword) "Hide" else "Show",
                    modifier = Modifier.clickable {
                        showPassword = !showPassword
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (
                    name.trim() == savedName.trim() &&
                    password == savedPassword &&
                    savedName.isNotEmpty()
                ) {
                    onLoginSuccess()
                } else {
                    error = "Name or password is incorrect."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text(
                text = "Log In",
                fontSize = 19.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// SIGN UP
// ======================================================

@Composable
fun SignUpScreen(
    onAccountCreated: () -> Unit,
    onBack: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    var name by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var error by remember { mutableStateOf("") }

    val picker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {
                imageUri = uri

                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) {
                }
            }
        }

    SimplePage {

        Text(
            text = "Create Your Profile ♡",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (imageUri == null) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFDCE8))
                    .clickable {
                        picker.launch(arrayOf("image/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "＋",
                    fontSize = 42.sp,
                    color = Color(0xFFB94E78)
                )
            }

        } else {

            SelectedProfileImage(
                uri = imageUri!!,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        picker.launch(arrayOf("image/*"))
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap to add profile picture",
            color = Color(0xFF9B6079)
        )

        Spacer(modifier = Modifier.height(18.dp))

        ProfileInput("Name", name) { name = it }
        ProfileInput("University Name", university) {
            university = it
        }
        ProfileInput("Year", year) {
            year = it
        }
        ProfileInput("Semester", semester) {
            semester = it
        }
        ProfileInput("Department", department) {
            department = it
        }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            singleLine = true,
            visualTransformation =
                if (showPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (showPassword) "Hide" else "Show",
                    modifier = Modifier.clickable {
                        showPassword = !showPassword
                    }
                )
            }
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {

                if (
                    name.isBlank() ||
                    university.isBlank() ||
                    year.isBlank() ||
                    semester.isBlank() ||
                    department.isBlank() ||
                    password.isBlank()
                ) {

                    error = "Please fill all fields."

                } else {

                    context
                        .getSharedPreferences(
                            "DeadlineBuddyProfile",
                            Context.MODE_PRIVATE
                        )
                        .edit()
                        .putBoolean("profileCreated", true)
                        .putString("name", name.trim())
                        .putString("university", university.trim())
                        .putString("year", year.trim())
                        .putString("semester", semester.trim())
                        .putString("department", department.trim())
                        .putString("password", password)
                        .putString(
                            "profileImage",
                            imageUri?.toString() ?: ""
                        )
                        .apply()

                    onAccountCreated()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text(
                text = "Create Profile ♡",
                fontSize = 19.sp
            )
        }

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// PROFILE
// ======================================================

@Composable
fun ProfileScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    var name by remember {
        mutableStateOf(prefs.getString("name", "") ?: "")
    }

    var university by remember {
        mutableStateOf(prefs.getString("university", "") ?: "")
    }

    var year by remember {
        mutableStateOf(prefs.getString("year", "") ?: "")
    }

    var semester by remember {
        mutableStateOf(prefs.getString("semester", "") ?: "")
    }

    var department by remember {
        mutableStateOf(prefs.getString("department", "") ?: "")
    }

    var imageUri by remember {
        mutableStateOf(
            prefs.getString("profileImage", "")
                ?.takeIf { it.isNotEmpty() }
                ?.let { Uri.parse(it) }
        )
    }

    val picker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {
                imageUri = uri

                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) {
                }
            }
        }

    SimplePage {

        Text(
            text = "My Profile ♡",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (imageUri != null) {
            SelectedProfileImage(
                uri = imageUri!!,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable {
                        picker.launch(arrayOf("image/*"))
                    }
            )
        } else {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFDCE8))
                    .clickable {
                        picker.launch(arrayOf("image/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "＋",
                    fontSize = 40.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        ProfileInput("Name", name) { name = it }
        ProfileInput("University Name", university) {
            university = it
        }
        ProfileInput("Year", year) { year = it }
        ProfileInput("Semester", semester) {
            semester = it
        }
        ProfileInput("Department", department) {
            department = it
        }

        Button(
            onClick = {

                prefs.edit()
                    .putString("name", name)
                    .putString("university", university)
                    .putString("year", year)
                    .putString("semester", semester)
                    .putString("department", department)
                    .putString(
                        "profileImage",
                        imageUri?.toString() ?: ""
                    )
                    .apply()

                onSaved()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text("Save Profile")
        }

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// ADD TASK
// ======================================================

@Composable
fun AddTaskScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var estimated by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    SimplePage {

        Text(
            text = "Add Task ✦",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileInput("Task Title", title) {
            title = it
        }

        ProfileInput("Description", description) {
            description = it
        }

        ProfileInput("Estimated Hours", estimated) {
            estimated = it
        }

        Text(
            text = "Smart difficulty: ${detectDifficulty(title, description)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF805568)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                if (title.isBlank()) {
                    message = "Please enter a task title."
                } else {

                    val old =
                        prefs.getString("tasks", "") ?: ""

                    prefs.edit()
                        .putString(
                            "tasks",
                            old + "\n• $title | ${detectDifficulty(title, description)} | $estimated hours"
                        )
                        .apply()

                    message = "Task saved successfully ♡"
                    title = ""
                    description = ""
                    estimated = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text("Save Task")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = Color(0xFFB94E78)
            )
        }

        TextButton(onClick = onBack) {
            Text("← Back to Dashboard")
        }
    }
}


// ======================================================
// ASSIGNMENT
// ======================================================

@Composable
fun AddAssignmentScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    SimplePage {

        Text(
            text = "Add Assignment ♡",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileInput("Assignment Title", title) {
            title = it
        }

        ProfileInput("Deadline", deadline) {
            deadline = it
        }

        ProfileInput("Estimated Hours", hours) {
            hours = it
        }

        Text(
            text = "Smart difficulty: ${detectDifficulty(title, "")}",
            color = Color(0xFF805568),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {

                if (title.isBlank()) {
                    message = "Please enter assignment title."
                } else {

                    val old =
                        prefs.getString("assignments", "") ?: ""

                    prefs.edit()
                        .putString(
                            "assignments",
                            old + "\n• $title | Due: $deadline | $hours hours"
                        )
                        .apply()

                    message = "Assignment saved ♡"
                    title = ""
                    deadline = ""
                    hours = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text("Save Assignment")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = message,
            color = Color(0xFFB94E78)
        )

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// REMINDER
// ======================================================

@Composable
fun ReminderScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {

    var reminder by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    SimplePage {

        Text(
            text = "Reminders ♡",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileInput("Reminder", reminder) {
            reminder = it
        }

        Button(
            onClick = {

                if (reminder.isNotBlank()) {

                    val old =
                        prefs.getString("reminders", "") ?: ""

                    prefs.edit()
                        .putString(
                            "reminders",
                            old + "\n• $reminder"
                        )
                        .apply()

                    message = "Reminder saved ♡"
                    reminder = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text("Save Reminder")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = message,
            color = Color(0xFFB94E78)
        )

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// CALENDAR
// ======================================================

@Composable
fun CalendarScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {

    val assignments =
        prefs.getString("assignments", "")
            ?: ""

    SimplePage {

        Text(
            text = "Calendar ♡",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5795C8)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your saved assignments:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF704A59)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text =
                if (assignments.isBlank())
                    "No assignments yet."
                else
                    assignments,
            fontSize = 15.sp,
            color = Color(0xFF806A73)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// DUE ASSIGNMENTS
// ======================================================

@Composable
fun DueAssignmentsScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {

    val assignments =
        prefs.getString("assignments", "")
            ?: ""

    SimplePage {

        Text(
            text = "Due Assignments",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5795C8)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text =
                if (assignments.isBlank())
                    "No assignments saved yet ♡"
                else
                    assignments,
            fontSize = 16.sp,
            color = Color(0xFF704A59)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// AUTO BREAKDOWN
// ======================================================

@Composable
fun AutoBreakdownScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    SimplePage {

        Text(
            text = "Auto Task Breakdown ✦",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB94E78)
        )

        Spacer(modifier = Modifier.height(15.dp))

        ProfileInput("Assignment / Project", title) {
            title = it
        }

        ProfileInput("Days Until Deadline", days) {
            days = it
        }

        Button(
            onClick = {

                val totalDays = days.toIntOrNull() ?: 0

                result =
                    createBreakdown(
                        title,
                        totalDays
                    )

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF58EAF)
            )
        ) {
            Text("Create Daily Plan")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (result.isNotEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEAF1)
                )
            ) {

                Text(
                    text = result,
                    modifier = Modifier.padding(18.dp),
                    fontSize = 15.sp,
                    color = Color(0xFF704A59)
                )
            }
        }

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}


// ======================================================
// HELPERS
// ======================================================

fun detectDifficulty(
    title: String,
    description: String
): String {

    val text =
        "$title $description"
            .lowercase(Locale.getDefault())

    val highWords = listOf(
        "research",
        "research paper",
        "thesis",
        "lab report",
        "laboratory",
        "project",
        "presentation",
        "final report",
        "case study"
    )

    val lowWords = listOf(
        "quiz",
        "revision",
        "review",
        "reading",
        "flashcard"
    )

    return when {
        highWords.any { text.contains(it) } ->
            "High effort"

        lowWords.any { text.contains(it) } ->
            "Low effort"

        else ->
            "Medium effort"
    }
}


fun createBreakdown(
    title: String,
    days: Int
): String {

    if (title.isBlank()) {
        return "Please enter an assignment name."
    }

    if (days <= 0) {
        return "Please enter a valid number of days."
    }

    if (days == 1) {
        return """
            Day 1
            • $title — Final work & submission
        """.trimIndent()
    }

    if (days == 2) {
        return """
            Day 1
            • $title — Outline & main work
            
            Day 2
            • $title — Review & final submission
        """.trimIndent()
    }

    if (days == 3) {
        return """
            Day 1
            • $title — Understand topic & outline
            
            Day 2
            • $title — Main draft / work
            
            Day 3
            • $title — Review & final submission
        """.trimIndent()
    }

    val middleDays = days - 2

    return buildString {

        appendLine("Day 1")
        appendLine("• $title — Research & outline")
        appendLine()

        for (i in 2..(middleDays + 1)) {
            appendLine("Day $i")
            appendLine("• $title — Main work / drafting")
            appendLine()
        }

        appendLine("Day $days")
        appendLine("• $title — Review, polish & submit")
    }
}


// ======================================================
// COMMON SIMPLE PAGE
// ======================================================

@Composable
fun SimplePage(
    content: @Composable ColumnScope.() -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F7))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}


// ======================================================
// PROFILE INPUT
// ======================================================

@Composable
fun ProfileInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = true
    )
}


// ======================================================
// PROFILE IMAGE
// ======================================================

@Composable
fun SelectedProfileImage(
    uri: Uri,
    modifier: Modifier
) {

    val context =
        androidx.compose.ui.platform.LocalContext.current

    var bitmap by remember(uri) {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    LaunchedEffect(uri) {

        try {

            context.contentResolver
                .openInputStream(uri)
                ?.use { stream ->
                    bitmap =
                        BitmapFactory.decodeStream(stream)
                }

        } catch (_: Exception) {
        }
    }

    if (bitmap != null) {

        Image(
            bitmap = bitmap!!.asImageBitmap(),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )

    } else {

        Box(
            modifier = modifier.background(
                Color(0xFFFFDCE8)
            ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "♡",
                fontSize = 40.sp
            )
        }
    }
}