package com.example.kabaddikounter.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.window.application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.Status
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.content.edit

class SharedViewModel( application: Application, scoreRepository: ScoreRepository) : AndroidViewModel(application) {
    val _score = MutableLiveData<Score>(scoreRepository.score.value)
    val repository = scoreRepository

    fun setScore(score: Score) {
        _score.value = score
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                repository.updateScore(score)
            }
    }
    }

    val teamA = MutableLiveData<String>("Team A")
    val teamB = MutableLiveData<String>("Team B")


    private val _scoreA = MutableLiveData<Int>(0)
    val scoreA:LiveData<Int> get() = _scoreA

    private val _scoreB = MutableLiveData<Int>(0)
    val scoreB:LiveData<Int> get() = _scoreB

    fun subscribeTopic(context: Context, topic: String){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            Log.d("Subscribe", "Subscribed to $topic")
            Toast.makeText(context, "Subscribed to $topic", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Log.e("Subscribe", "Failed to subscribed to $topic")
        }
    }

    fun unsubscribeTopic(context: Context, topic:String){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener {
            Log.d("Unsubscribe", "Unubscribed from $topic")
        }.addOnFailureListener {
            Log.e("Unsubscribe", "Failde to unsubscribed from $topic")

        }
    }

    fun changeTopic(score: Score) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(application)
        setScore(score)

        val oldTopic = prefs.getString("current_topic", "")
        val newTopic = score.status.toString()

        oldTopic?.let {
            unsubscribeTopic(
                application, it
            )
        }

        subscribeTopic(application, newTopic)
        prefs.edit {
            putString("current_topic", newTopic)
        }

    }

}