package com.example.deadlinebuddy

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

private val Pink = Color(0xFFFFE5F0)
private val DarkPink = Color(0xFFCC5D91)
private val Purple = Color(0xFFEDE7FF)
private val Cream = Color(0xFFFFF8E8)
private val TextDark = Color(0xFF493746)
private val SoftBackground = Color(0xFFFFF9FC)

private data class BuddyBadge(
    val title: String,
    val emoji: String,
    val requiredCount: Int,
    val description: String
)

private val buddyBadges = listOf(
    BuddyBadge("Early Bird", "🌱", 1, "Save your first assignment"),
    BuddyBadge("Task Explorer", "🐾", 5, "Save 5 assignments"),
    BuddyBadge("Study Star", "⭐", 7, "Save 7 assignments"),
    BuddyBadge("Super Planner", "🎀", 9, "Save 9 assignments"),
    BuddyBadge("Deadline Buddy", "🏆", 10, "Save 10 assignments")
)

@Composable
fun CuteDashboardScreen(
    userName: String,
    onAddTask: () -> Unit,
    onAddAssignment: () -> Unit,
    onCalendar: () -> Unit,
    onDueAssignments: () -> Unit,
    onReminder: () -> Unit,
    onAutoTaskBreakdown: () -> Unit,
    onProfile: () -> Unit
) {
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences(
            "DeadlineBuddyProfile",
            Context.MODE_PRIVATE
        )
    }

    var assignmentCount by remember {
        mutableIntStateOf(prefs.getInt("assignmentCount", 0))
    }

    var profileImagePath by remember {
        mutableStateOf(prefs.getString("profileImage", "") ?: "")
    }

    DisposableEffect(prefs) {
        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
                when (key) {
                    "assignmentCount" -> {
                        assignmentCount = sharedPrefs.getInt(
                            "assignmentCount",
                            0
                        )
                    }

                    "profileImage" -> {
                        profileImagePath =
                            sharedPrefs.getString("profileImage", "") ?: ""
                    }
                }
            }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        assignmentCount = prefs.getInt("assignmentCount", 0)
        profileImagePath = prefs.getString("profileImage", "") ?: ""

        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    val profileBitmap = remember(profileImagePath) {
        try {
            if (profileImagePath.isBlank()) {
                null
            } else {
                if (profileImagePath.startsWith("content://")) {
                    context.contentResolver
                        .openInputStream(Uri.parse(profileImagePath))
                        ?.use { input ->
                            BitmapFactory.decodeStream(input)
                        }
                } else {
                    val imageFile = File(profileImagePath)
                    if (imageFile.exists()) {
                        BitmapFactory.decodeFile(profileImagePath)
                    } else {
                        null
                    }
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    val unlockedCount = buddyBadges.count {
        assignmentCount >= it.requiredCount
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hello, $userName! ♡",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Ready to make today productive?",
                    fontSize = 14.sp,
                    color = Color(0xFF8D7887)
                )
            }

            IconButton(
                onClick = onProfile,
                modifier = Modifier
                    .size(58.dp)
                    .background(Pink, CircleShape)
            ) {
                if (profileBitmap != null) {
                    Image(
                        bitmap = profileBitmap.asImageBitmap(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = DarkPink,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Pink
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your little study buddy 🐱",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "One small step at a time. You got this!",
                        fontSize = 14.sp,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onAddAssignment,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkPink
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("Add assignment")
                    }
                }

                Text("🎀", fontSize = 42.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your dashboard",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardTile(
                title = "Assignments",
                subtitle = "$assignmentCount saved",
                emoji = "📚",
                color = Purple,
                modifier = Modifier.weight(1f),
                onClick = onAddAssignment
            )

            DashboardTile(
                title = "Reminders",
                subtitle = "Stay on time",
                emoji = "🔔",
                color = Cream,
                modifier = Modifier.weight(1f),
                onClick = onReminder
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardTile(
                title = "Calendar",
                subtitle = "View your dates",
                emoji = "📅",
                color = Cream,
                modifier = Modifier.weight(1f),
                onClick = onCalendar
            )

            DashboardTile(
                title = "Due tasks",
                subtitle = "Check deadlines",
                emoji = "⏰",
                color = Purple,
                modifier = Modifier.weight(1f),
                onClick = onDueAssignments
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        DashboardTile(
            title = "Task breakdown",
            subtitle = "Plan your study steps",
            emoji = "📝",
            color = Pink,
            modifier = Modifier.fillMaxWidth(),
            onClick = onAutoTaskBreakdown
        )

        Spacer(modifier = Modifier.height(12.dp))

        // NEW DAILY TO-DO CARD
        DashboardTile(
            title = "Daily To-do",
            subtitle = "Add and complete your daily tasks",
            emoji = "🎀",
            color = Color(0xFFFFDCEB),
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddTask
        )

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "My Badges 🏆",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    text = "Every assignment brings you closer!",
                    fontSize = 13.sp,
                    color = Color(0xFF8D7887)
                )
            }

            Text(
                text = "$unlockedCount / ${buddyBadges.size}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPink
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        buddyBadges.forEach { badge ->
            val unlocked = assignmentCount >= badge.requiredCount

            BadgeCard(
                badge = badge,
                unlocked = unlocked,
                assignmentCount = assignmentCount
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Keep going, you're doing great! 🌷",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 13.sp,
            color = Color(0xFF9A8292)
        )
    }
}

@Composable
private fun BadgeCard(
    badge: BuddyBadge,
    unlocked: Boolean,
    assignmentCount: Int
) {
    val progress = (
            assignmentCount.toFloat() / badge.requiredCount.toFloat()
            ).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) Pink else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (unlocked) badge.emoji else "🔒",
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = if (unlocked) {
                        "Unlocked! ${badge.description} ♡"
                    } else {
                        "${badge.description} ($assignmentCount/${badge.requiredCount})"
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF8D7887)
                )

                if (!unlocked) {
                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = DarkPink,
                        trackColor = Color(0xFFF1D9E5)
                    )
                }
            }

            if (unlocked) {
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Unlocked",
                    tint = DarkPink
                )
            }
        }
    }
}

@Composable
private fun DashboardTile(
    title: String,
    subtitle: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(emoji, fontSize = 28.sp)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color(0xFF8D7887)
            )
        }
    }
}