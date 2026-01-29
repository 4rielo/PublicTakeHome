package com.ascarafia.publictakehome.data.datasources

import androidx.sqlite.SQLiteException
import com.ascarafia.publictakehome.data.database.TaskDao
import com.ascarafia.publictakehome.data.mappers.toTask
import com.ascarafia.publictakehome.data.mappers.toTaskEntity
import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.EmptyResult
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.model.Task

class TaskLocalDataSource(
    private val taskDao: TaskDao
): TaskDataSource {

    override suspend fun getTasks(): Result<List<Task>, DataError> {
        try {
            val tasksList = taskDao
                .getTasks()
                .map { taskEntity ->
                    taskEntity.toTask()
                }
            return Result.Success(tasksList)
        } catch (e: Exception) {
            return Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getTask(id: String): Result<Task, DataError> {
        val task = taskDao.getTask(id)?.toTask()
        return if(task != null) {
            Result.Success(task)
        } else {
            Result.Error(DataError.Local.NOT_FOUND)
        }
    }

    override suspend fun upsertTask(task: Task): EmptyResult<DataError> {
        try {
            taskDao.upsertTask(task.toTaskEntity())
            return Result.Success(Unit)
        } catch(e: SQLiteException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String): EmptyResult<DataError> {
        try {
            taskDao.deleteTask(id)
            return Result.Success(Unit)
        } catch(e: SQLiteException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }
}