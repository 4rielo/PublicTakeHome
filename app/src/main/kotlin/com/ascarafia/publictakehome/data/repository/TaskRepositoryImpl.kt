package com.ascarafia.publictakehome.data.repository

import com.ascarafia.publictakehome.data.mappers.toTask
import com.ascarafia.publictakehome.data.mappers.toTaskEntity
import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> {
        return taskDataSource.getTasks().map { tasks ->
            tasks.map { it.toTask() }
        }
    }

    override suspend fun getTask(id: String): Task? {
        return taskDataSource.getTask(id)?.toTask()
    }

    override suspend fun upsertTask(task: Task): Result<Unit, DataError> {
        return try {
            taskDataSource.upsertTask(task.toTaskEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit, DataError> {
        return try {
            taskDataSource.deleteTask(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}