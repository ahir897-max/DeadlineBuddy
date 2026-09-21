package com.example.deadlinebuddy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7FA))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        // TITLE

        item {

            Text(
                text = "Deadline Buddy ♥",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5795C8),
                modifier = Modifier.padding(
                    start = 8.dp,
                    top = 8.dp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Let's make your deadlines stress-free 🌸",
                fontSize = 17.sp,
                color = Color(0xFF604B58),
                modifier = Modifier.padding(start = 8.dp)
            )
        }


        // PROFILE + HELLO KITTY

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    modifier = Modifier
                        .weight(0.85f)
                        .height(225.dp)
                        .clickable {
                            onProfile()
                        },
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE6EF)
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Hello, $userName! ♥",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB94E78),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "My Profile",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF704A59)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tap to view\nor edit profile ♡",
                            fontSize = 14.sp,
                            color = Color(0xFF856873),
                            textAlign = TextAlign.Center
                        )
                    }
                }


                Card(
                    modifier = Modifier
                        .weight(1.15f)
                        .height(225.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFE6EF)
                    )
                ) {

                    Image(
                        painter = painterResource(
                            id = R.drawable.hello_kitty_dashboard
                        ),
                        contentDescription = "Hello Kitty Dashboard",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }


        // STREAK

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE1EA)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(
                            id = R.drawable.cute_cat_mascot
                        ),
                        contentDescription = "Cute Cat",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(62.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = "Study Streak 🔥",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8A4F67)
                        )

                        Text(
                            text = "Start today and build your streak!",
                            fontSize = 14.sp,
                            color = Color(0xFF765E68)
                        )

                        Text(
                            text = "🏆 On-time badge available",
                            fontSize = 13.sp,
                            color = Color(0xFF9A7180)
                        )
                    }
                }
            }
        }


        // FIRST ROW

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CuteButton(
                    icon = "＋",
                    title = "Add Task",
                    background = Color(0xFFFFDCE8),
                    modifier = Modifier.weight(1f),
                    onClick = onAddTask
                )

                CuteButton(
                    icon = "▣",
                    title = "Add Assignment",
                    background = Color(0xFFE8DDF8),
                    modifier = Modifier.weight(1f),
                    onClick = onAddAssignment
                )
            }
        }


        // SECOND ROW

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CuteButton(
                    icon = "▦",
                    title = "Calendar",
                    background = Color(0xFFDDEFFC),
                    modifier = Modifier.weight(1f),
                    onClick = onCalendar
                )

                CuteButton(
                    icon = "✓",
                    title = "Due Assignments",
                    background = Color(0xFFFFE8C7),
                    modifier = Modifier.weight(1f),
                    onClick = onDueAssignments
                )
            }
        }


        // THIRD ROW

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CuteButton(
                    icon = "♧",
                    title = "Reminder",
                    background = Color(0xFFE1F3E7),
                    modifier = Modifier.weight(1f),
                    onClick = onReminder
                )

                CuteButton(
                    icon = "✦",
                    title = "Auto Task\nBreakdown",
                    background = Color(0xFFFFE4B8),
                    modifier = Modifier.weight(1f),
                    onClick = onAutoTaskBreakdown
                )
            }
        }


        // SMART FEATURES TITLE

        item {

            Text(
                text = "♡ Smart Study Buddy",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF704A59),
                modifier = Modifier.padding(
                    start = 6.dp,
                    top = 5.dp
                )
            )
        }


        // SELF LEARNING

        item {

            SmartFeatureCard(
                title = "Self-Learning Estimates",
                description = "The app compares estimated and actual study time and can improve future suggestions.",
                emoji = "🧠"
            )
        }


        // SMART DIFFICULTY

        item {

            SmartFeatureCard(
                title = "Smart Difficulty Detection",
                description = "Task keywords help estimate effort automatically. Research papers and lab reports need more effort than simple quiz prep.",
                emoji = "✨"
            )
        }


        // AUTO BREAKDOWN

        item {

            SmartFeatureCard(
                title = "Automatic Task Breakdown",
                description = "Large assignments can be divided into daily goals such as outline, draft, review and submission.",
                emoji = "🌸"
            )
        }


        // BADGES

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF0D9)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(
                            id = R.drawable.cute_cat_mascot
                        ),
                        contentDescription = "Cute Cat Mascot",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = "🏆 Badges",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF795548)
                        )

                        Text(
                            text = "🌸 Early Bird\n🔥 3-Day Streak\n⭐ 7-Day Streak\n💗 On-Time Student",
                            fontSize = 14.sp,
                            color = Color(0xFF806A73)
                        )
                    }
                }
            }
        }


        // TODAY'S TASKS

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEAF1)
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.cute_cat_mascot
                            ),
                            contentDescription = "Cute Cat",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(55.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Today's Tasks ♥",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF704A59)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Add a task and your study buddy will keep track of it ♡",
                        fontSize = 14.sp,
                        color = Color(0xFF856873)
                    )
                }
            }
        }


        item {

            Text(
                text = "Small steps every day lead to big dreams ♡",
                fontSize = 15.sp,
                color = Color(0xFF9B687A),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 5.dp,
                        bottom = 25.dp
                    )
            )
        }
    }
}


// ======================================================
// CUTE BUTTON
// ======================================================

@Composable
fun CuteButton(
    icon: String,
    title: String,
    background: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(135.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(13.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = icon,
                    fontSize = 27.sp,
                    color = Color(0xFF76566B)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Image(
                    painter = painterResource(
                        id = R.drawable.cute_cat_mascot
                    ),
                    contentDescription = "Cute Cat Mascot",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(43.dp)
                )
            }

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF604B58)
            )
        }
    }
}


// ======================================================
// SMART FEATURE CARD
// ======================================================

@Composable
fun SmartFeatureCard(
    title: String,
    description: String,
    emoji: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.cute_cat_mascot
                ),
                contentDescription = "Cute Cat Mascot",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "$emoji  $title",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF704A59)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color(0xFF806A73),
                    lineHeight = 18.sp
                )
            }
        }
    }
}