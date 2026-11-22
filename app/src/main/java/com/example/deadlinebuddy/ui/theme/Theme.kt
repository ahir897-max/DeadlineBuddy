package com.example.deadlinebuddy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PinkLavenderColorScheme = lightColorScheme(
    primary = Color(0xFFD96FA5),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFF9B83CC),
    onSecondary = Color(0xFFFFFFFF),

    tertiary = Color(0xFFE9A5C8),
    onTertiary = Color(0xFF493746),

    background = Color(0xFFFFEAF3),
    onBackground = Color(0xFF493746),

    surface = Color(0xFFFFF4F9),
    onSurface = Color(0xFF493746),

    surfaceVariant = Color(0xFFEDE4FA),
    onSurfaceVariant = Color(0xFF665675),

    outline = Color(0xFFD8B9D6)
)

@Composable
fun DeadlineBuddyTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PinkLavenderColorScheme,
        typography = Typography,
        content = content
    )
}