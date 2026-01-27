package com.ascarafia.publictakehome.domain.datasources

import com.ascarafia.publictakehome.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun getTasks(): Flow<List<Task>>
    suspend fun getTask(id: String): Task?
    suspend fun upsertTask(task: Task)
    suspend fun deleteTask(id: String)
}