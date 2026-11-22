package com.example.deadlinebuddy

data class AssignmentData(
    val id: String,
    val title: String,
    val subject: String,
    val dueDate: Long,
    val description: String,
    val imagePath: String? = null
)


