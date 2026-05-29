package com.example.kabaddikounter

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.kabaddikounter.database.AppDatabase
import com.example.kabaddikounter.datasource.ScoreLocalSource
import com.example.kabaddikounter.datasource.LiveMatchStore
import com.example.kabaddikounter.repository.ScoreRepository
import com.example.kabaddikounter.repository.LiveMatchRepository
import com.example.kabaddikounter.network.LiveScoreApiClient
import com.example.kabaddikounter.service.LiveScoreNotificationHelper
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    private val scoreLocalSource by lazy { ScoreLocalSource(database.scoreDao()) }
    val scoreRepository by lazy { ScoreRepository(scoreLocalSource) }
    private val liveMatchStore by lazy { LiveMatchStore(this) }
    val liveMatchRepository by lazy {
        LiveMatchRepository(LiveScoreApiClient.liveMatchApi, liveMatchStore)
    }

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        LiveScoreNotificationHelper.createChannels(this)

        val isDarkMode = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.pref_key_dark_mode), false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}