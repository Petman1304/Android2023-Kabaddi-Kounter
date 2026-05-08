package com.example.kabaddikounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(scoreRepository: ScoreRepository) : ViewModel() {

    val repository = scoreRepository

    val teamA = MutableLiveData<String>("Team A")
    val teamB = MutableLiveData<String>("Team B")

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _scoreA = MutableLiveData<Int>(0)
    val scoreA:LiveData<Int> get() = _scoreA

    private val _scoreB = MutableLiveData<Int>(0)
    val scoreB:LiveData<Int> get() = _scoreB


    fun incrementScoreA(points: Int = 1) {
        _scoreA.value = _scoreA.value!! + points
    }

    fun incrementScoreB(points: Int = 1) {
        _scoreB.value = _scoreB.value!! + points
    }

    fun reset() {
        _scoreA.value = 0;
        _scoreB.value = 0;
//        teamA.value = "";
//        teamB.value = "";
    }

    fun insertScore() = viewModelScope.launch {
        val score = Score(
            teamId = 0,
            teamAName = teamA.value.toString(),
            teamBName = teamB.value.toString(),
            teamAScore = scoreA.value,
            teamBScore = scoreB.value,
            timestamp = null,
        )
        withContext(Dispatchers.IO){
            repository.insertScore(score)
        }
        _toastMessage.value = "Score saved to history!"
    }

    fun onToastShown(){
        _toastMessage.value = null
    }
}