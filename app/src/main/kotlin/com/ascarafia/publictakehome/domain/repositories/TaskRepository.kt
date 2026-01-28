package com.ascarafia.publictakehome.domain.repositories

import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun getTask(id: String): Task?
    suspend fun upsertTask(task: Task): Result<Unit, DataError>
    suspend fun deleteTask(id: String): Result<Unit, DataError>
}