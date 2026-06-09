package com.example.kabaddikounter.service

import android.R
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.Status
import com.example.kabaddikounter.data.entities.Score
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import com.example.kabaddikounter.Converters


class FCMService: FirebaseMessagingService() {
    override fun onNewToken(token: String){
        Log.d("FCM", "token: $token")
        super.onNewToken(token)
    }

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    val converters = Converters()

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    fun subscribeTopic(context: Context, topic: String){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            Toast.makeText(context, "Subscribed to $topic", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to subscribe to $topic", Toast.LENGTH_SHORT).show()
        }
    }

    fun unsubscribeTopic(context: Context, topic:String){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener {
            Toast.makeText(context, "Unsubscribed to $topic", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to unsubscribed to $topic", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val repository = (application as MyApplication).scoreRepository


        val title = if(message.data["type"] == "match_end") {
            "Match Ended: ${message.data["teamAName"]} vs ${message.data["teamBName"]}"
        } else {
            "${message.data["teamAName"]} vs ${message.data["teamBName"]}"
        }

        val body = "Score: ${message.data["teamAScore"]} - ${message.data["teamBScore"]}"

//        val newScore = Score(
//        message.data["matchId"]!!.toInt(),
//        message.data["teamAName"],
//        message.data["teamBName"],
//        message.data["teamAScore"]?.toIntOrNull(),
//        message.data["teamBName"]?.toIntOrNull(),
//        Status.END
//        )
//        repository.updateScore(newScore)


        serviceScope.launch {
            val newScore = Score(
                message.data["matchId"]!!.toInt(),
                message.data["teamAName"],
                message.data["teamBName"],
                message.data["teamAScore"]?.toIntOrNull(),
                message.data["teamBScore"]?.toIntOrNull(),
                converters.toStatus(message.data["status"]!!)
            )
            Log.d("FCM", newScore.toString())
            repository.updateScore(newScore)
        }


        val notification = NotificationCompat.Builder(applicationContext, "ForegroundServiceChannel")
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())


    }
}