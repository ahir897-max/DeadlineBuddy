package com.example.deadlinebuddy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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
import androidx.core.content.ContextCompat
import com.example.deadlinebuddy.ui.theme.DeadlineBuddyTheme
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

    var refresh by remember { mutableIntStateOf(0) }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    LaunchedEffect(Unit) {
        AlarmScheduler.scheduleAll(context)

        if (Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    DeadlineBuddyTheme {
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
                            prefs.getString("name", "Student") ?: "Student"
                        screen = "dashboard"
                    },
                    onBack = { screen = "home" }
                )
            }

            "signup" -> {
                SignUpScreen(
                    onAccountCreated = {
                        userName =
                            prefs.getString("name", "Student") ?: "Student"
                        screen = "dashboard"
                    },
                    onBack = { screen = "home" }
                )
            }

            "dashboard" -> {
                key(refresh) {
                    CuteDashboardScreen(
                        userName = userName,
                        onAddTask = { screen = "dailyTodo" },
                        onAddAssignment = { screen = "assignment" },
                        onCalendar = { screen = "calendar" },
                        onDueAssignments = { screen = "dueAssignments" },
                        onReminder = { screen = "reminder" },
                        onAutoTaskBreakdown = { screen = "autoTask" },
                        onProfile = { screen = "profile" }
                    )
                }
            }

            "dailyTodo" -> {
                DailyTodoScreen(
                    onBackClick = {
                        screen = "dashboard"
                    }
                )
            }

            "profile" -> {
                ProfileScreen(
                    prefs = prefs,
                    onBack = { screen = "dashboard" },
                    onSaved = {
                        userName =
                            prefs.getString("name", "Student") ?: "Student"
                        refresh++
                    }
                )
            }

            "addTask" -> {
                AddTaskScreen(
                    context = context,
                    onBackClick = { screen = "dashboard" }
                )
            }

            "assignment" -> {
                AddAssignmentScreen(
                    onBackClick = { screen = "dashboard" }
                )
            }

            "reminder" -> {
                AlarmScreen(
                    onBack = { screen = "dashboard" }
                )
            }

            "calendar" -> {
                CalendarScreen(
                    onBackClick = { screen = "dashboard" }
                )
            }

            "dueAssignments" -> {
                DueTasksScreen(
                    onBack = { screen = "dashboard" }
                )
            }

            "autoTask" -> {
                AutoTaskBreakdownScreen(
                    onBack = { screen = "dashboard" }
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
            .background(Color(0xFFFFEAF3))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "Deadline Buddy ♥",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9B83CC)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.hello_kitty_mew),
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
                containerColor = Color(0xFFD96FA5)
            )
        ) {
            Text("Log In →", fontSize = 20.sp)
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
                "Sign Up ♡",
                fontSize = 20.sp,
                color = Color(0xFF9B83CC)
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
            color = Color(0xFF9B83CC)
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
                if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
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
            Text(text = error, color = Color.Red)
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
                containerColor = Color(0xFFD96FA5)
            )
        ) {
            Text("Log In", fontSize = 19.sp)
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

    val picker = rememberLauncherForActivityResult(
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
            color = Color(0xFF9B83CC)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (imageUri == null) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE4FA))
                    .clickable { picker.launch(arrayOf("image/*")) },
                contentAlignment = Alignment.Center
            ) {
                Text("＋", fontSize = 42.sp, color = Color(0xFF9B83CC))
            }
        } else {
            SelectedProfileImage(
                uri = imageUri!!,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { picker.launch(arrayOf("image/*")) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap to add profile picture",
            color = Color(0xFF9B83CC)
        )

        Spacer(modifier = Modifier.height(18.dp))

        ProfileInput("Name", name) { name = it }
        ProfileInput("University Name", university) { university = it }
        ProfileInput("Year", year) { year = it }
        ProfileInput("Semester", semester) { semester = it }
        ProfileInput("Department", department) { department = it }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            singleLine = true,
            visualTransformation =
                if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
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
            Text(text = error, color = Color.Red)
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
                    context.getSharedPreferences(
                        "DeadlineBuddyProfile",
                        Context.MODE_PRIVATE
                    ).edit()
                        .putBoolean("profileCreated", true)
                        .putString("name", name.trim())
                        .putString("university", university.trim())
                        .putString("year", year.trim())
                        .putString("semester", semester.trim())
                        .putString("department", department.trim())
                        .putString("password", password)
                        .putString("profileImage", imageUri?.toString() ?: "")
                        .apply()

                    onAccountCreated()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD96FA5)
            )
        ) {
            Text("Create Profile ♡", fontSize = 19.sp)
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

    val picker = rememberLauncherForActivityResult(
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
            color = Color(0xFF9B83CC)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (imageUri != null) {
            SelectedProfileImage(
                uri = imageUri!!,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { picker.launch(arrayOf("image/*")) }
            )
        } else {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDE4FA))
                    .clickable { picker.launch(arrayOf("image/*")) },
                contentAlignment = Alignment.Center
            ) {
                Text("＋", fontSize = 40.sp)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        ProfileInput("Name", name) { name = it }
        ProfileInput("University Name", university) { university = it }
        ProfileInput("Year", year) { year = it }
        ProfileInput("Semester", semester) { semester = it }
        ProfileInput("Department", department) { department = it }

        Button(
            onClick = {
                prefs.edit()
                    .putString("name", name)
                    .putString("university", university)
                    .putString("year", year)
                    .putString("semester", semester)
                    .putString("department", department)
                    .putString("profileImage", imageUri?.toString() ?: "")
                    .apply()

                onSaved()
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD96FA5)
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
// OLD REMINDER SCREEN - KEPT FOR COMPATIBILITY
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
            color = Color(0xFF9B83CC)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileInput("Reminder", reminder) { reminder = it }

        Button(
            onClick = {
                if (reminder.isNotBlank()) {
                    val old = prefs.getString("reminders", "") ?: ""

                    prefs.edit()
                        .putString("reminders", old + "\n• $reminder")
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
                containerColor = Color(0xFFD96FA5)
            )
        ) {
            Text("Save Reminder")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = message, color = Color(0xFF9B83CC))

        TextButton(onClick = onBack) {
            Text("← Back")
        }
    }
}

// ======================================================
// OLD AUTO BREAKDOWN - KEPT FOR COMPATIBILITY
// ======================================================

@Composable
fun AutoBreakdownScreen(
    prefs: android.content.SharedPreferences,
    onBack: () -> Unit
) {
    SimplePage {
        Text(
            text = "Auto Task Breakdown",
            fontSize = 25.sp,
            color = Color(0xFF9B83CC)
        )
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
    val text = "$title $description".lowercase(Locale.getDefault())

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
        highWords.any { text.contains(it) } -> "High effort"
        lowWords.any { text.contains(it) } -> "Low effort"
        else -> "Medium effort"
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
            .background(Color(0xFFFFEAF3))
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
        label = { Text(label) },
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
    val context = androidx.compose.ui.platform.LocalContext.current

    var bitmap by remember(uri) {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    LaunchedEffect(uri) {
        try {
            context.contentResolver
                .openInputStream(uri)
                ?.use { stream ->
                    bitmap = BitmapFactory.decodeStream(stream)
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
            modifier = modifier.background(Color(0xFFEDE4FA)),
            contentAlignment = Alignment.Center
        ) {
            Text("♡", fontSize = 40.sp)
        }
    }
}