package com.ascarafia.publictakehome.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query("SELECT * FROM TaskEntity")
    suspend fun getTasks(): List<TaskEntity>

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    suspend fun getTask(id: String): TaskEntity?

    @Query("DELETE FROM TaskEntity WHERE id = :id")
    suspend fun deleteTask(id: String)
}