package com.example.kabaddikounter.service

import android.R
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService: FirebaseMessagingService() {
    override fun onNewToken(token: String){
        Log.d("FCM", "token: $token")
        super.onNewToken(token)
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

        val notification = NotificationCompat.Builder(applicationContext, "ForegroundServiceChannel")
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)

        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification.build())
    }
}