package com.ascarafia.publictakehome.data.repository

import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import com.ascarafia.publictakehome.domain.use_cases.DateTimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

class TaskRepositoryImpl(
    private val tasksLocalDataSource: TaskDataSource,
    private val tasksRemoteDataSource: TaskDataSource,
    private val repositoryScope: CoroutineScope
) : TaskRepository {

    //Tasks remote list are updated/synced when start, and after 5 minutes of repository flow not being subscribed to
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = _taskList
        .onStart {
            sync()
            updateTaskList()
        }
        .stateIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(
                5.minutes
            ),
            initialValue = emptyList()
        )

    override fun getTasks(): Flow<List<Task>> {
        return taskList
    }

    override suspend fun getTask(id: String): Task? {
        val taskResponse = tasksLocalDataSource.getTask(id)
        return if(taskResponse is Result.Success) {
            taskResponse.data
        } else {
            null
        }
    }

    override suspend fun upsertTask(task: Task): Result<Unit, DataError> {
        return try {
            val updatedTime = DateTimeUtils.toIsoString(Clock.System.now())
            tasksLocalDataSource.upsertTask(task.copy(lastUpdated = updatedTime))
            updateTaskList()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit, DataError> {
        return try {
            val deletedTask = tasksLocalDataSource.getTask(id)
            if(deletedTask is Result.Success) {
                val task = deletedTask.data.copy(isDeleted = true)
                tasksLocalDataSource.upsertTask(task)
            }
            //tasksLocalDataSource.deleteTask(id) //DEPRECATED - use soft delete instead
            updateTaskList()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun refreshTasks() {
        sync()
        updateTaskList()
    }

    private suspend fun updateTaskList() {
        val response = tasksLocalDataSource.getTasks()
        if(response is Result.Success) {
            _taskList.value = response.data.filter { !it.isDeleted }
        } else if(response is Result.Error) {
            _taskList.value = emptyList<Task>()
        }
    }

    private suspend fun sync() {
        withContext(Dispatchers.IO) {
            val localTasks = tasksLocalDataSource.getTasks()
            val remoteTasks = tasksRemoteDataSource.getTasks()
            if (localTasks is Result.Success && remoteTasks is Result.Success) {
                val localTasksList = localTasks.data
                val remoteTasksList = remoteTasks.data

                val remoteTasksToUpdate = mutableListOf<Task>()
                val localTasksToUpdate = mutableListOf<Task>()

                for (localTask in localTasksList) {
                    val matchingRemoteTask = remoteTasksList.firstOrNull { it.id == localTask.id }
                    matchingRemoteTask?.let {
                        val localLastUpdated = DateTimeUtils.fromIsoString(localTask.lastUpdated)
                        val remoteLastUpdated = DateTimeUtils.fromIsoString(matchingRemoteTask.lastUpdated)

                        remoteLastUpdated?.let {
                            if( (localLastUpdated?.compareTo(remoteLastUpdated) ?: -1) > 0 ) {
                                remoteTasksToUpdate.add(localTask)
                            } else {
                                localTasksToUpdate.add(matchingRemoteTask)
                            }
                        } ?: run {
                            val localLastUpdated = DateTimeUtils.toIsoString(Clock.System.now())
                            localTasksToUpdate.add( matchingRemoteTask.copy(lastUpdated = localLastUpdated ) )
                        }

                    } ?: run {
                        remoteTasksToUpdate.add(localTask)
                    }
                }

                for (remoteTask in remoteTasksList) {
                    val matchingLocalTask = localTasksList.firstOrNull { it.id == remoteTask.id }
                    matchingLocalTask?.let {
                        val localLastUpdated = DateTimeUtils.fromIsoString(matchingLocalTask.lastUpdated)
                        val remoteLastUpdated = DateTimeUtils.fromIsoString(remoteTask.lastUpdated)

                        remoteLastUpdated?.let {
                            if ((localLastUpdated?.compareTo(remoteLastUpdated) ?: -1) > 0) {
                                remoteTasksToUpdate.add(matchingLocalTask)
                            } else {
                                localTasksToUpdate.add(matchingLocalTask)
                            }
                        } ?: run {
                            val localLastUpdated = DateTimeUtils.toIsoString(Clock.System.now())
                            localTasksToUpdate.add(matchingLocalTask.copy(lastUpdated = localLastUpdated))
                        }

                    } ?: run {
                        localTasksToUpdate.add(remoteTask)
                    }
                }

                for (remoteTask in remoteTasksToUpdate) {
                    tasksRemoteDataSource.upsertTask(remoteTask)
                }

                for (localTask in localTasksToUpdate) {
                    tasksLocalDataSource.upsertTask(localTask)
                }
            }
        }
    }
}