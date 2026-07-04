package com.pedro.cinelog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WatchedEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CineLogDatabase : RoomDatabase() {

    abstract fun watchedEntryDao(): WatchedEntryDao

    companion object {
        @Volatile
        private var instancia: CineLogDatabase? = null

        fun obterInstancia(context: Context): CineLogDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    CineLogDatabase::class.java,
                    "cinelog_database"
                ).build().also { instancia = it }
            }
        }
    }
}
