package com.example.kabaddikounter.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kabaddikounter.Status
import com.example.kabaddikounter.data.entities.Score

class SharedViewModel : ViewModel() {
    val _score = MutableLiveData<Score>(Score(0, "Team A", "Team B", 0, 0, Status.OFFLINE))

    fun setScore(score: Score) {
        _score.value = score
    }

    val teamA = MutableLiveData<String>("Team A")
    val teamB = MutableLiveData<String>("Team B")


    private val _scoreA = MutableLiveData<Int>(0)
    val scoreA:LiveData<Int> get() = _scoreA

    private val _scoreB = MutableLiveData<Int>(0)
    val scoreB:LiveData<Int> get() = _scoreB

}