package com.ascarafia.publictakehome.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

class DatabaseFactory(
    private val context: Context
) {
    fun create(): RoomDatabase.Builder<TaskDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(TaskDatabase.DATABASE_NAME)

        return Room.databaseBuilder(
            context = appContext,
            klass = TaskDatabase::class.java,
            name = dbFile.absolutePath
        )
    }
}