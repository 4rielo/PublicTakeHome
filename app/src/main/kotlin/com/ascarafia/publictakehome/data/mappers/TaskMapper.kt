package com.ascarafia.publictakehome.data.mappers

import com.ascarafia.publictakehome.data.database.TaskEntity
import com.ascarafia.publictakehome.data.network.model.TaskDto
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.use_cases.DateTimeUtils
import kotlin.time.Clock

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt,
        isPinned = isPinned,
        isDeleted = isDeleted,
        lastUpdated = lastUpdated
    )
}

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt,
        isPinned = isPinned,
        isDeleted = isDeleted,
        lastUpdated = lastUpdated
    )
}

fun TaskDto.toTask(): Task {
    return Task(
        id = id.orEmpty(),
        title = title.orEmpty().take(50),
        description = description.orEmpty().take(200),
        isCompleted = isCompleted ?: false,
        createdAt = createdAt.orEmpty(),
        isPinned = isPinned ?: false,
        isDeleted = isDeleted ?: false,
        lastUpdated = lastUpdated ?: DateTimeUtils.toIsoString(Clock.System.now())
    )
}

fun Task.toTaskDto(): TaskDto {
    return TaskDto(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdAt = createdAt,
        isPinned = isPinned,
        isDeleted = isDeleted,
        lastUpdated = lastUpdated
    )
}