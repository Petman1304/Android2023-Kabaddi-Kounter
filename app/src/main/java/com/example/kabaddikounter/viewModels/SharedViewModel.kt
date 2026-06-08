package com.example.kabaddikounter.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.Status
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(scoreRepository: ScoreRepository) : ViewModel() {
    val _score = MutableLiveData<Score>(scoreRepository.score.value)
    val repository = scoreRepository

    fun setScore(score: Score) {
        _score.value = score
        viewModelScope.launch{
            withContext(Dispatchers.Main){
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

}