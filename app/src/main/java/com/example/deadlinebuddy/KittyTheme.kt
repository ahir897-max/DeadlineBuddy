package com.example.deadlinebuddy

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object KittyColors {
    val Pink = Color(0xFFFF8FB1)
    val LightPink = Color(0xFFFFE1EC)
    val BabyPink = Color(0xFFFFF0F5)
    val White = Color(0xFFFFFFFF)
    val Red = Color(0xFFE85D75)
    val DarkPink = Color(0xFFB83260)
    val Text = Color(0xFF65384C)
    val Muted = Color(0xFFB08094)
    val Purple = Color(0xFFE9D9FF)
    val Green = Color(0xFFD7F5DF)
    val Yellow = Color(0xFFFFF0BD)
}

private val KittyColorScheme = lightColorScheme(
    primary = KittyColors.Pink,
    onPrimary = KittyColors.White,
    secondary = KittyColors.DarkPink,
    onSecondary = KittyColors.White,
    background = KittyColors.BabyPink,
    onBackground = KittyColors.Text,
    surface = KittyColors.White,
    onSurface = KittyColors.Text,
    error = KittyColors.Red
)

@Composable
fun KittyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KittyColorScheme,
        typography = Typography(
            headlineLarge = androidx.compose.ui.text.TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = KittyColors.DarkPink
            ),
            titleLarge = androidx.compose.ui.text.TextStyle(
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = KittyColors.DarkPink
            ),
            bodyLarge = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                color = KittyColors.Text
            )
        ),
        content = content
    )
}

@Composable
fun KittyCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                KittyColors.LightPink,
                RoundedCornerShape(22.dp)
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = KittyColors.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun KittyHeader(
    title: String,
    subtitle: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "🎀 $title 🎀",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = KittyColors.DarkPink
        )

        if (subtitle.isNotBlank()) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = subtitle,
                color = KittyColors.Text,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun KittyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = KittyColors.Pink,
            contentColor = KittyColors.White
        )
    ) {
        Text(
            text = "🎀 $text",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun KittyDifficultyBadge(
    difficulty: KittyDifficulty
) {
    val (label, color) = when (difficulty) {
        KittyDifficulty.EASY -> "EASY 🌸" to KittyColors.Green
        KittyDifficulty.MEDIUM -> "MEDIUM 🎀" to KittyColors.Yellow
        KittyDifficulty.HARD -> "HARD 💗" to KittyColors.LightPink
    }

    Surface(
        color = color,
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 7.dp
            ),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = KittyColors.DarkPink
        )
    }
}