package com.ascarafia.publictakehome.data.datasources

import com.ascarafia.publictakehome.data.mappers.toTask
import com.ascarafia.publictakehome.data.mappers.toTaskDto
import com.ascarafia.publictakehome.data.network.model.TaskDto
import com.ascarafia.publictakehome.data.network.safeCall
import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.EmptyResult
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.Task
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TaskRemoteDataSource(
    private val httpClient: HttpClient,
): TaskDataSource {
    override suspend fun getTasks(): Result<List<Task>, DataError> {
        val response: Result<List<TaskDto>, DataError> = safeCall {
            httpClient.get (
                "https://jsonplaceholder.typicode.com/todos"
            )
        }
        return when(response) {
            is Result.Success -> {
                Result.Success(response.data.map { it.toTask() })
            }
            is Result.Error -> {
                Result.Error(response.error)
            }
        }
    }

    override suspend fun getTask(id: String): Result<Task, DataError> {
        val response: Result<TaskDto, DataError> = safeCall {
            httpClient.get (
                "https://jsonplaceholder.typicode.com/todos/$id"
            )
        }
        return when(response) {
            is Result.Success -> {
                Result.Success(response.data.toTask() )
            }
            is Result.Error -> {
                Result.Error(response.error)
            }
        }
    }

    override suspend fun upsertTask(task: Task): EmptyResult<DataError> {
        val response: Result<TaskDto, DataError> = safeCall {
            httpClient.post (
                "https://jsonplaceholder.typicode.com/todos"
            ) {
                header("Content-Type", "application/json; charset=UTF-8")           //required by jsonplaceholder
                setBody(task.toTaskDto())
            }
        }
        return when(response) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Error -> {
                Result.Error(response.error)
            }
        }
    }

    override suspend fun deleteTask(id: String): EmptyResult<DataError> {
        val response: Result<TaskDto, DataError> = safeCall {
            httpClient.delete (
                "https://jsonplaceholder.typicode.com/todos/$id"
            )
        }
        return when(response) {
            is Result.Success -> {
                Result.Success(Unit)
            }
            is Result.Error -> {
                Result.Error(response.error)
            }
        }
    }
}