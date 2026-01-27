package com.ascarafia.publictakehome.data.repository

import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> {
        return taskDataSource.getTasks()
    }

    override suspend fun getTask(id: String): Task? {
        return taskDataSource.getTask(id)
    }

    override suspend fun upsertTask(task: Task) {
        return taskDataSource.upsertTask(task)
    }

    override suspend fun deleteTask(id: String) {
        return taskDataSource.deleteTask(id)
    }
}