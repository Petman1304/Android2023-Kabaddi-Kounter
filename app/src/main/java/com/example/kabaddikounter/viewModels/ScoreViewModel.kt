package com.example.kabaddikounter.viewModels

import android.app.Application
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.data.live.LiveMatch
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class ScoreViewModel(
    application: Application,
    private val repository: ScoreRepository,
) : AndroidViewModel(application) {

    val allScore: LiveData<List<Score>> = repository.allScore.asLiveData()

    val teamA = MutableLiveData("Team C")
    val teamB = MutableLiveData("Team D")

    private val _scoreA = MutableLiveData(0)
    val scoreA: LiveData<Int>
        get() = _scoreA

    private val _scoreB = MutableLiveData(0)
    val scoreB: LiveData<Int>
        get() = _scoreB

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _liveMatchLocked = MutableLiveData(false)
    val liveMatchLocked: LiveData<Boolean> get() = _liveMatchLocked

    init {
        viewModelScope.launch {
            val lastScore: Score? = repository.getLatestScore()

            lastScore?.let { score ->
                teamA.value = score.teamAName.toString()
                teamB.value = score.teamBName.toString()
                _scoreA.value = score.teamAScore ?: 0
                _scoreB.value = score.teamBScore ?: 0
            } ?: run {
                teamA.value = "Team A"
                teamB.value = "Team B"
                _scoreA.value = 0
                _scoreB.value = 0
            }
        }
    }

    fun incrementScoreA(points: Int = 1) {
        if (_liveMatchLocked.value == true) return
        _scoreA.value = _scoreA.value!! + points
    }

    fun incrementScoreB(points: Int = 1) {
        if (_liveMatchLocked.value == true) return
        _scoreB.value = _scoreB.value!! + points
    }

    fun reset() {
        _scoreA.value = 0
        _scoreB.value = 0
        teamA.value = "Team A"
        teamB.value = "Team B"
        _liveMatchLocked.value = false
    }

    fun applyLiveMatch(match: LiveMatch?) {
        if (match == null) {
            _liveMatchLocked.value = false
            return
        }

        teamA.value = match.teamAName
        teamB.value = match.teamBName
        _scoreA.value = match.teamAScore
        _scoreB.value = match.teamBScore
        _liveMatchLocked.value = true
    }

    fun insertScore() = viewModelScope.launch {
        if (_liveMatchLocked.value == true) return@launch

        val score = Score(
            teamId = 0,
            teamAName = teamA.value.toString(),
            teamBName = teamB.value.toString(),
            teamAScore = scoreA.value,
            teamBScore = scoreB.value,
            timestamp = null,
        )
        withContext(Dispatchers.IO) {
            repository.insertScore(score)
        }
    }

    fun scoresToJson(): String {
        val gson = Gson()
        val data = allScore.value

        return if (data != null) {
            gson.toJson(data)
        } else {
            "[]"
        }
    }

    private fun getRandomFileName(): String {
        return Calendar.getInstance().timeInMillis.toString() + ".json"
    }

    fun writeToJsonFile() {
        val jsonText = scoresToJson()
        if (jsonText.isNotEmpty()) {
            val dir = getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                ?: getApplication<Application>().filesDir
            val extFile = File(dir, getRandomFileName())
            try {
                FileOutputStream(extFile).use { fos ->
                    fos.write(jsonText.toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            _toastMessage.value = "File saved. $extFile"
        }
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

}
