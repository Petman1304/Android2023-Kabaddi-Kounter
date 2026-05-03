package com.example.kabaddikounter

import android.app.Application
import com.example.kabaddikounter.database.AppDatabase
import com.example.kabaddikounter.datasource.ScoreLocalSource
import com.example.kabaddikounter.repository.ScoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    private val scoreLocalSource by lazy { ScoreLocalSource(database.scoreDao()) }
    val scoreRepository by lazy { ScoreRepository(scoreLocalSource) }
}