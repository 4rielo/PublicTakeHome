package com.ascarafia.publictakehome.domain.datasources

import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.EmptyResult
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.Task

interface TaskDataSource {
    suspend fun getTasks(): Result<List<Task>, DataError>
    suspend fun getTask(id: String): Result<Task, DataError>
    suspend fun upsertTask(task: Task): EmptyResult<DataError>
    suspend fun deleteTask(id: String): EmptyResult<DataError>
}