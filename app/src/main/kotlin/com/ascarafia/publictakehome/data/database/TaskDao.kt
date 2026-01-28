package com.ascarafia.publictakehome.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query("SELECT * FROM taskentity")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM taskentity WHERE id = :id")
    suspend fun getTask(id: String): TaskEntity?

    @Query("DELETE FROM taskentity WHERE id = :id")
    suspend fun deleteTask(id: String)
}