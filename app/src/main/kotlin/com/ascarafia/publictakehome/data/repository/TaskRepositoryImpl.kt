package com.ascarafia.publictakehome.data.repository

import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val repositoryScope: CoroutineScope
) : TaskRepository {

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList
        .onStart {
            updateTaskList()
        }
        .stateIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun getTasks(): Flow<List<Task>> {
        return taskList
    }

    override suspend fun getTask(id: String): Task? {
        val taskResponse = taskDataSource.getTask(id)
        return if(taskResponse is Result.Success) {
            taskResponse.data
        } else {
            null
        }
    }

    override suspend fun upsertTask(task: Task): Result<Unit, DataError> {
        return try {
            taskDataSource.upsertTask(task)
            updateTaskList()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit, DataError> {
        return try {
            taskDataSource.deleteTask(id)
            updateTaskList()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    private suspend fun updateTaskList() {
        val response = taskDataSource.getTasks()
        if(response is Result.Success) {
            _taskList.value = response.data
        } else if(response is Result.Error) {
            _taskList.value = emptyList<Task>()
        }
    }
}