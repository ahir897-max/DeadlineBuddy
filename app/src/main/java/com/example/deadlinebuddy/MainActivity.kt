package com.example.deadlinebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DeadlineBuddyApp()
        }
    }
}

@Composable
fun DeadlineBuddyApp() {

    var screen by remember {
        mutableStateOf("welcome")
    }

    when (screen) {

        "welcome" -> {
            WelcomeScreen(
                onCreateClick = {
                    screen = "profile"
                }
            )
        }

        "profile" -> {
            ProfileScreen(
                onNextClick = {
                    screen = "dashboard"
                }
            )
        }

        "dashboard" -> {
            DashboardScreen()
        }
    }
}


// --------------------------------------------------
// WELCOME SCREEN
// --------------------------------------------------

@Composable
fun WelcomeScreen(
    onCreateClick: () -> Unit
) {

    val transition = rememberInfiniteTransition(
        label = "cat"
    )

    val catScale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "catScale"
    )

    val pink = Color(0xFFFFF0F6)
    val darkPink = Color(0xFFE85D8E)
    val textColor = Color(0xFF553746)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pink)
    ) {

        Text(
            text = "🌸  🌷  🌸",
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 45.dp)
        )

        Text(
            text = "🌼",
            fontSize = 38.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(30.dp)
        )

        Text(
            text = "🌷",
            fontSize = 38.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(30.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "🐱",
                fontSize = 125.sp,
                modifier = Modifier.scale(catScale)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "DeadlineBuddy",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = darkPink
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Your cute little study planner 💕",
                fontSize = 17.sp,
                color = textColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(35.dp))

            Button(
                onClick = onCreateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkPink
                )
            ) {

                Text(
                    text = "Create My Buddy ✨",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "🌸     🌷     🌸",
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 35.dp)
        )
    }
}


// --------------------------------------------------
// PROFILE SCREEN
// --------------------------------------------------

@Composable
fun ProfileScreen(
    onNextClick: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }

    val pink = Color(0xFFFFF0F6)
    val darkPink = Color(0xFFE85D8E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pink)
            .padding(24.dp)
    ) {

        Text(
            text = "🌸 Create Your Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = darkPink
        )

        Text(
            text = "Let's get to know you 💕",
            fontSize = 16.sp,
            modifier = Modifier.padding(
                top = 6.dp,
                bottom = 20.dp
            )
        )

        ProfileInput(
            value = name,
            onValueChange = { name = it },
            label = "Name",
            placeholder = "Your name"
        )

        ProfileInput(
            value = university,
            onValueChange = { university = it },
            label = "University Name",
            placeholder = "Your university"
        )

        ProfileInput(
            value = department,
            onValueChange = { department = it },
            label = "Department",
            placeholder = "e.g. CSE"
        )

        ProfileInput(
            value = year,
            onValueChange = { year = it },
            label = "Year",
            placeholder = "e.g. 3rd Year"
        )

        ProfileInput(
            value = semester,
            onValueChange = { semester = it },
            label = "Semester",
            placeholder = "e.g. 1st Semester"
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkPink
            )
        ) {

            Text(
                text = "Next →",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// --------------------------------------------------
// PROFILE INPUT
// --------------------------------------------------

@Composable
fun ProfileInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}


// --------------------------------------------------
// DASHBOARD
// --------------------------------------------------

@Composable
fun DashboardScreen() {

    val pink = Color(0xFFFFF0F6)
    val darkPink = Color(0xFFE85D8E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pink)
            .padding(20.dp)
    ) {

        Text(
            text = "Hello, Buddy! 🐱💕",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = darkPink
        )

        Text(
            text = "Let's make your deadlines stress-free 🌸",
            fontSize = 16.sp,
            modifier = Modifier.padding(
                top = 5.dp,
                bottom = 20.dp
            )
        )

        DashboardButton("📝", "Add Task")
        DashboardButton("📚", "Add Assignment")
        DashboardButton("🗓️", "Calendar")
        DashboardButton("⏰", "Due Assignments")
        DashboardButton("🔔", "Reminder")
    }
}


// --------------------------------------------------
// DASHBOARD BUTTON
// --------------------------------------------------

@Composable
fun DashboardButton(
    emoji: String,
    title: String
) {

    Button(
        onClick = {
            // Functionality will be added next
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(65.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Text(
            text = "$emoji   $title",
            fontSize = 18.sp
        )
    }
}