package com.example.kabaddikounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.Converters
import com.example.kabaddikounter.Status
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(scoreRepository: ScoreRepository) : ViewModel() {
    val repository = scoreRepository

    val score: LiveData<Score> = repository.score

    val teamA = MutableLiveData<String>(score.value?.teamAName)
    val teamB = MutableLiveData<String>(score.value?.teamBName)

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _scoreA = MutableLiveData<Int>(score.value?.teamAScore)
    val scoreA:LiveData<Int> get() = _scoreA

    private val _scoreB = MutableLiveData<Int>(score.value?.teamBScore)
    val scoreB:LiveData<Int> get() = _scoreB

    private val _status = MutableLiveData<String>(score.value?.status.toString())
    val status: LiveData<String> get() = _status

    private val _btnEnable = MutableLiveData<Boolean>(true)
    val btnEnable : LiveData<Boolean> get() = _btnEnable

    fun getScore() : Score {
        return Score(
            id = 0,
            teamAName = teamA.value,
            teamBName = teamB.value,
            teamAScore = scoreA.value,
            teamBScore = scoreB.value,
            status = Converters().toStatus(status.value!!)
        )
    }

    fun updateScore() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                repository.updateScore(getScore())
            }
        }
    }


    fun incrementScoreA(points: Int = 1) {
        _scoreA.value = _scoreA.value!! + points
    }

    fun incrementScoreB(points: Int = 1) {
        _scoreB.value = _scoreB.value!! + points
    }

    fun reset() {
        _scoreA.value = 0;
        _scoreB.value = 0;
        teamA.value = "";
        teamB.value = "";
        _status.value = Status.OFFLINE.toString()
        updateBtn(_status.value.toString())
    }

    fun insertScore() = viewModelScope.launch {
        val score = Score(
            id = 0,
            teamAName = teamA.value.toString(),
            teamBName = teamB.value.toString(),
            teamAScore = scoreA.value,
            teamBScore = scoreB.value,
            status = Status.OFFLINE
        )
        withContext(Dispatchers.IO){
            repository.insertScore(score)
        }
        _toastMessage.value = "Score saved to history!"
    }

    fun onToastShown(){
        _toastMessage.value = null
    }

    fun loadData(match: Score?) {
        teamA.value = match?.teamAName.toString()
        teamB.value = match?.teamBName.toString()
        _scoreA.value = match?.teamAScore?.toInt()
        _scoreB.value = match?.teamBScore?.toInt()
        _status.value = match?.status.toString()
        updateBtn(status.value.toString())
    }

    fun updateBtn(status: String) {
        _btnEnable.value = status != "LIVE"
    }
}