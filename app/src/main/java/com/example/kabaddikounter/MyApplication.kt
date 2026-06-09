package com.example.kabaddikounter

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.database.AppDatabase
import com.example.kabaddikounter.datasource.ScoreLocalSource
import com.example.kabaddikounter.repository.ScoreRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class MyApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    private val scoreLocalSource by lazy { ScoreLocalSource(database.scoreDao()) }
    val scoreRepository by lazy { ScoreRepository(scoreLocalSource) }
    lateinit var prefs : SharedPreferences

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "ForegroundServiceChannel",
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onTerminate() {
        super.onTerminate()
        val topic = prefs.getString("current_topic", "")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic!!)
    }

}