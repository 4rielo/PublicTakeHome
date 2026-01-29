package com.ascarafia.publictakehome.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object DatabaseMigrations {
    //MIGRATION from v1 to v2, added "isDeleted" field to TaskEntity
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL("ALTER TABLE TaskEntity ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
            connection.execSQL("ALTER TABLE TaskEntity ADD COLUMN lastUpdated TEXT NOT NULL DEFAULT ''")
        }
    }
}