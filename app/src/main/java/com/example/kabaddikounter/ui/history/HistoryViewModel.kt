package com.example.kabaddikounter.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.repository.ScoreRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class HistoryViewModel(scoreRepository: ScoreRepository) : ViewModel() {

    lateinit var allScore: LiveData<List<Score>>
    val repository = scoreRepository

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    init {
        viewModelScope.launch {
            addScoretoList()
        }
    }

    fun addScoretoList() = viewModelScope.launch {
        allScore = repository.allScore.asLiveData()
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

    fun reset(){
        return
    }
}