package com.example.kabaddikounter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val teamA = MutableLiveData<String>("Team A")
    val teamB = MutableLiveData<String>("Team B")

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
}