package com.example.kabaddikounter.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.R
import androidx.lifecycle.Observer

class LiveScoreService : Service() {
    private val channelId = "ForegroundServiceChannel"
    private val repository by lazy {
        (application as MyApplication).scoreRepository
    }
    private val scoreObserver = Observer<Int> {
        score -> updateNotification()
    }
    private val nameObserver = Observer<String> {
        name -> updateNotification()
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action){
            Action.START.toString() -> start()
            Action.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        repository.scoreA.removeObserver(scoreObserver)
        repository.scoreB.removeObserver(scoreObserver)

        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun start(){
        repository.scoreA.observeForever(scoreObserver)
        repository.scoreB.observeForever(scoreObserver)


    }

    fun updateNotification(){
        val content = "${repository.teamA.value.toString()} ${repository.scoreA.value.toString()} - ${repository.scoreB.value.toString()} ${repository.teamB.value.toString()}"
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Live Match")
            .setContentText(content)
            .build()
        startForeground(1, notification)
    }

    enum class Action{
        START, STOP
    }
}