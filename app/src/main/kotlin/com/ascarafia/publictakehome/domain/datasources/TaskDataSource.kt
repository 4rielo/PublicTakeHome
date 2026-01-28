package com.ascarafia.publictakehome.domain.datasources

import com.ascarafia.publictakehome.data.database.TaskEntity
import com.ascarafia.publictakehome.domain.model.DataError
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun getTasks(): Flow<List<TaskEntity>>
    suspend fun getTask(id: String): TaskEntity?
    suspend fun upsertTask(task: TaskEntity): Result<DataError?>
    suspend fun deleteTask(id: String): Result<DataError?>
}