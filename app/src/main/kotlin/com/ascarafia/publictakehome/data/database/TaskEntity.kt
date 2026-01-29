package com.ascarafia.publictakehome.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: String,
    val isPinned: Boolean = false,
    val isDeleted: Boolean = false,
    val lastUpdated: String,
)