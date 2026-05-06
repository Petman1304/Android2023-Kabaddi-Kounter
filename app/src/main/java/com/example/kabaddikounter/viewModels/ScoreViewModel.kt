package com.example.kabaddikounter.viewModels

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar


class ScoreViewModel(private val sharedPreferences: SharedPreferences, private val repository: ScoreRepository) : ViewModel() {

    val darkMode = MutableLiveData<Boolean>(false)

    lateinit var allScore: LiveData<List<Score>>

    val teamA = repository.teamA
    val teamB = repository.teamB

    private val _scoreA = repository.scoreA
    val scoreA: LiveData<Int>
        get() = _scoreA

    private val _scoreB = repository.scoreB
    val scoreB: LiveData<Int>
        get() = _scoreB

    init {
        darkMode.value = sharedPreferences.getBoolean("isDarkMode", false)
        addScoretoList()
        viewModelScope.launch {
            val lastScore : Score? = repository.getLatestScore()

            lastScore?.let {
                score ->
                teamA.value = score.teamAName.toString()
                teamB.value = score.teamBName.toString()
                _scoreA.value = score.teamAScore !!
                _scoreB.value = score.teamBScore !!
            } ?:run{
                teamA.value = "Team A"
                teamB.value = "Team B"
                _scoreA.value = 0
                _scoreB.value = 0
            }

        }

    }

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun incrementScoreA(points: Int = 1) {
        _scoreA.value = _scoreA.value!! + points
    }

    fun incrementScoreB(points: Int = 1) {
        _scoreB.value = _scoreB.value!! + points
    }

    fun changeMode(darkMode: Boolean){
        Log.d("DarkMode", darkMode.toString())
        if(darkMode == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        sharedPreferences.edit {
            putBoolean("isDarkMode", darkMode)
        }
    }

    fun reset() {
        _scoreA.value = 0;
        _scoreB.value = 0;
        teamA.value = "";
        teamB.value = "";
    }

    fun addScoretoList() = viewModelScope.launch {
        allScore = repository.allScore.asLiveData()
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
    }

    fun deleteAllScore() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            repository.deleteAll()
        }
    }

    fun scoresToJson() : String? {
        val gson = Gson()
        val data = allScore.value

        return if (data!=null) {
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
        if(jsonText != ""){
            val dir = File("//sdcard//Documents//")
            val extFile = File(dir, getRandomFileName())
            var fos : FileOutputStream? = null
            try{
                fos = FileOutputStream(extFile)
                fos.write(jsonText?.toByteArray())
                fos.close()
            } catch (e: IOException){
                e.printStackTrace()
            }
            _toastMessage.value = "File saved. $extFile"
        }
    }

    fun onToastShown(){
        _toastMessage.value = null
    }

}
