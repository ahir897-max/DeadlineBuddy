package com.example.deadlinebuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit
) {
    var selectedDate by remember {
        mutableStateOf("No date selected")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🗓️ Calendar",
            fontSize = 28.sp
        )

        Text(
            text = "Selected date: $selectedDate",
            fontSize = 18.sp,
            modifier = Modifier.padding(20.dp)
        )

        Button(
            onClick = {
                selectedDate = "Today"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Select Today")
        }

        Button(
            onClick = {
                selectedDate = "Tomorrow"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Select Tomorrow")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("← Back")
        }
    }
}