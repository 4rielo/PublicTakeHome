package com.ascarafia.publictakehome.domain.model

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: String,
    val isPinned: Boolean = false
)
