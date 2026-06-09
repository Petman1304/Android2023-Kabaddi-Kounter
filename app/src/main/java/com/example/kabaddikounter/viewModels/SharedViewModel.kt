package com.example.kabaddikounter.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.Status
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(scoreRepository: ScoreRepository) : ViewModel() {
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

}