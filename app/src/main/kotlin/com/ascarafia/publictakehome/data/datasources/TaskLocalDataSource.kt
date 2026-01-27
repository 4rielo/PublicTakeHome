package com.ascarafia.publictakehome.data.datasources

import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TaskLocalDataSource: TaskDataSource {
    val tasks = mutableListOf<Task>(
        Task(
            id = "1",
            title = "Task 1",
            description = "Description 1",
            isCompleted = false,
            createdAt = "2023-01-01",
        ),
        Task(
            id = "2",
            title = "Task 2",
            description = "Description 2, but this is waaaaay bigger, because it takes up to a few rows.",
            isCompleted = false,
            createdAt = "2023-01-01",
        ),
        Task(
            id = "3",
            title = "Task 3",
            description = "Description 3, and yes, this is slightly larger than the first one.",
            isCompleted = true,
            createdAt = "2023-01-01",
        ),
        Task(
            id = "4",
            title = "Task 4",
            description = "Description 4, yet another task.",
            isCompleted = false,
            createdAt = "2023-01-01",
        ),
    )
    override fun getTasks(): Flow<List<Task>> {
        return flowOf(tasks)
    }

    override suspend fun getTask(id: String): Task? {
        return tasks.find { it.id == id }
    }

    override suspend fun upsertTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun deleteTask(id: String) {
        tasks.removeIf { it.id == id }
    }
}