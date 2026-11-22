package com.example.deadlinebuddy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.example.deadlinebuddy.R

val GochiHand = FontFamily(
    Font(R.font.gochi_hand, FontWeight.Normal)
)

val Pacifico = FontFamily(
    Font(R.font.pacifico, FontWeight.Normal)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Pacifico,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Pacifico,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Pacifico,
        fontSize = 36.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Pacifico,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Pacifico,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Pacifico,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = GochiHand,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = GochiHand,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = GochiHand,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = GochiHand,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GochiHand,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = GochiHand,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = GochiHand,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = GochiHand,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = GochiHand,
        fontSize = 12.sp
    )
)