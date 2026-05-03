package com.example.kabaddikounter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kabaddikounter.dao.ScoreDao
import com.example.kabaddikounter.data.entities.Score
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Score::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            applicationScope: CoroutineScope
        ):AppDatabase{
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "score_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}