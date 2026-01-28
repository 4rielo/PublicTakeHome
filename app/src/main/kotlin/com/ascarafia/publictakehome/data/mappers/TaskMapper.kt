package com.ascarafia.publictakehome.data.mappers

import com.ascarafia.publictakehome.data.database.TaskEntity
import com.ascarafia.publictakehome.domain.model.Task

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt,
        isPinned = isPinned
    )
}

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt,
        isPinned = isPinned
    )
}