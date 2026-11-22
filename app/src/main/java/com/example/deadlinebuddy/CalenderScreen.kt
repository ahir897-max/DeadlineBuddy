package com.example.deadlinebuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val CalendarPink = Color(0xFFF58EAF)
private val CalendarDarkPink = Color(0xFFB94E78)
private val CalendarBackground = Color(0xFFFFF8F7)
private val CalendarSoftPink = Color(0xFFFFEAF1)

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    var refresh by remember { mutableIntStateOf(0) }

    val assignments = remember(refresh) {
        BuddyData.getAssignments(context)
    }

    var displayedMonth by remember {
        mutableStateOf(Calendar.getInstance())
    }

    var selectedDate by remember {
        mutableStateOf(
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(Date())
        )
    }

    val monthYear = SimpleDateFormat(
        "MMMM yyyy",
        Locale.getDefault()
    ).format(displayedMonth.time)

    val selectedAssignments = assignments.filter {
        getAssignmentDate(it.deadline) == selectedDate
    }

    val markedDates = assignments.mapNotNull {
        getAssignmentDate(it.deadline)
    }.toSet()

    val firstDay = (displayedMonth.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = displayedMonth.getActualMaximum(
        Calendar.DAY_OF_MONTH
    )

    val startOffset = (
            firstDay.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY
            )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CalendarBackground)
            .padding(18.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text(
                "← Back",
                color = CalendarDarkPink,
                fontSize = 16.sp
            )
        }

        Text(
            "My Calendar 🎀",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CalendarDarkPink
        )

        Text(
            "All your assignment deadlines in one place ♡",
            color = Color(0xFF96717F),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            displayedMonth = (
                                    displayedMonth.clone() as Calendar
                                    ).apply {
                                    add(Calendar.MONTH, -1)
                                }
                        }
                    ) {
                        Text("‹ Previous", color = CalendarDarkPink)
                    }

                    Text(
                        monthYear,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CalendarDarkPink
                    )

                    TextButton(
                        onClick = {
                            displayedMonth = (
                                    displayedMonth.clone() as Calendar
                                    ).apply {
                                    add(Calendar.MONTH, 1)
                                }
                        }
                    ) {
                        Text("Next ›", color = CalendarDarkPink)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val weekDays = listOf(
                    "Sun", "Mon", "Tue", "Wed",
                    "Thu", "Fri", "Sat"
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    weekDays.forEach { day ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                day,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CalendarDarkPink
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val totalCells = ((startOffset + daysInMonth + 6) / 7) * 7

                for (week in 0 until totalCells / 7) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (dayIndex in 0..6) {
                            val cellIndex = week * 7 + dayIndex
                            val dayNumber = cellIndex - startOffset + 1

                            val validDay =
                                dayNumber in 1..daysInMonth

                            val dateKey = if (validDay) {
                                val temp = (
                                        displayedMonth.clone() as Calendar
                                        ).apply {
                                        set(Calendar.DAY_OF_MONTH, dayNumber)
                                    }

                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(temp.time)
                            } else {
                                ""
                            }

                            val isSelected =
                                validDay && dateKey == selectedDate

                            val hasAssignment =
                                validDay && dateKey in markedDates

                            val todayKey = SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            ).format(Date())

                            val isToday =
                                validDay && dateKey == todayKey

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(3.dp)
                                    .aspectRatio(1f)
                                    .background(
                                        color = when {
                                            isSelected -> CalendarPink
                                            isToday -> CalendarSoftPink
                                            else -> Color.Transparent
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable(enabled = validDay) {
                                        selectedDate = dateKey
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (validDay) {
                                    Column(
                                        horizontalAlignment =
                                            Alignment.CenterHorizontally,
                                        verticalArrangement =
                                            Arrangement.Center
                                    ) {
                                        Text(
                                            "$dayNumber",
                                            color = if (isSelected) {
                                                Color.White
                                            } else {
                                                Color(0xFF704A59)
                                            },
                                            fontWeight = if (
                                                isSelected || isToday
                                            ) FontWeight.Bold
                                            else FontWeight.Normal
                                        )

                                        if (hasAssignment) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 2.dp)
                                                    .size(5.dp)
                                                    .background(
                                                        if (isSelected) {
                                                            Color.White
                                                        } else {
                                                            CalendarDarkPink
                                                        },
                                                        RoundedCornerShape(50)
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .background(
                                CalendarDarkPink,
                                RoundedCornerShape(50)
                            )
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        "Assignment deadline",
                        fontSize = 12.sp,
                        color = Color(0xFF96717F)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            "Assignments on $selectedDate",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = CalendarDarkPink
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedAssignments.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CalendarSoftPink
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎀", fontSize = 30.sp)

                    Text(
                        "No assignments on this date!",
                        color = CalendarDarkPink,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        "Enjoy your free time ♡",
                        color = Color(0xFF96717F),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(selectedAssignments, key = { it.id }) { assignment ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp)
                        ) {
                            Text(
                                assignment.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = CalendarDarkPink
                            )

                            Text(
                                "📚 ${assignment.subject}",
                                color = Color(0xFF704A59)
                            )

                            if (assignment.description.isNotBlank()) {
                                Text(
                                    assignment.description,
                                    color = Color(0xFF96717F),
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                "⏰ ${assignment.deadline}",
                                color = CalendarDarkPink,
                                fontSize = 13.sp
                            )

                            Text(
                                if (assignment.done) {
                                    "✓ Completed"
                                } else {
                                    "♡ Pending"
                                },
                                color = if (assignment.done) {
                                    Color(0xFF4C9B71)
                                } else {
                                    CalendarDarkPink
                                },
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getAssignmentDate(deadline: String): String? {
    val formats = listOf(
        "dd/MM/yyyy HH:mm",
        "dd/MM/yyyy hh:mm a",
        "yyyy-MM-dd HH:mm"
    )

    for (format in formats) {
        try {
            val date = SimpleDateFormat(
                format,
                Locale.getDefault()
            ).apply {
                isLenient = false
            }.parse(deadline)

            if (date != null) {
                return SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(date)
            }
        } catch (_: Exception) {
            // Try the next date format.
        }
    }

    return null
}