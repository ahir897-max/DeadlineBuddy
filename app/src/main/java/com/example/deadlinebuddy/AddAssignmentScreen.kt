package com.example.deadlinebuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddAssignmentScreen(
    onBackClick: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val pink = Color(0xFFFFF0F6)
    val darkPink = Color(0xFFE85D8E)
    val textColor = Color(0xFF553746)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pink)
            .padding(24.dp)
    ) {

        Text(
            text = "📚 Add Assignment",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = darkPink
        )

        Text(
            text = "Keep track of your assignments 💕",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(
                top = 6.dp,
                bottom = 20.dp
            )
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Assignment Title") },
            placeholder = { Text("e.g. Database Assignment") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") },
            placeholder = { Text("e.g. CSE 2201") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = dueDate,
            onValueChange = { dueDate = it },
            label = { Text("Due Date") },
            placeholder = { Text("e.g. 20 September 2026") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Write assignment details") },
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Save functionality later
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkPink
            )
        ) {
            Text(
                text = "Save Assignment 💕",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "← Back to Dashboard",
                color = darkPink
            )
        }
    }
}

