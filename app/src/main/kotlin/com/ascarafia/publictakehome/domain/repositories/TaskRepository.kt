package com.ascarafia.publictakehome.domain.repositories

import com.ascarafia.publictakehome.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun getTask(id: String): Task?
    suspend fun upsertTask(task: Task)
    suspend fun deleteTask(id: String)
}