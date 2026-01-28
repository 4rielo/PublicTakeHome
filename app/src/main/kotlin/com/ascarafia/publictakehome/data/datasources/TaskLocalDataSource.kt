package com.ascarafia.publictakehome.data.datasources

import androidx.sqlite.SQLiteException
import com.ascarafia.publictakehome.data.database.TaskDao
import com.ascarafia.publictakehome.data.database.TaskEntity
import com.ascarafia.publictakehome.domain.datasources.TaskDataSource
import com.ascarafia.publictakehome.domain.model.DataError
import kotlinx.coroutines.flow.Flow

class TaskLocalDataSource(
    private val taskDao: TaskDao
): TaskDataSource {

    override fun getTasks(): Flow<List<TaskEntity>> {
        return taskDao.getTasks()
    }

    override suspend fun getTask(id: String): TaskEntity? {
        return taskDao.getTask(id)
    }

    override suspend fun upsertTask(task: TaskEntity): Result<DataError?> {
        try {
            taskDao.upsertTask(task)
            return Result.success(null)
        } catch(e: SQLiteException) {
            return Result.success(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteTask(id: String): Result<DataError?> {
        try {
            taskDao.deleteTask(id)
            return Result.success(null)
        } catch(e: SQLiteException) {
            return Result.success(DataError.Local.DISK_FULL)
        }
    }
}