package com.example.kabaddikounter.repository

import androidx.lifecycle.MutableLiveData
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.datasource.ScoreLocalSource
import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreLocalSource: ScoreLocalSource) {

    val allScore: Flow<List<Score>> = scoreLocalSource.getAllScore()
    val teamA = MutableLiveData<String>("Team A")
    fun getTeamA(): String = teamA.value.toString()
    fun setTeamA(name : String){ teamA.value = name }

    val teamB = MutableLiveData<String>("Team B")
    fun getTeamB(): String = teamB.value.toString()
    fun setTeamB(name : String){ teamB.value = name }

    val scoreA = MutableLiveData<Int>(0)
    fun getScoreA() : Int = scoreA.value!!
    fun setScoreA(score : Int) {scoreA.value = score}

    val scoreB = MutableLiveData<Int>(0)
    fun getScoreB() : Int = scoreB.value!!
    fun setScoreB(score : Int) {scoreB.value = score}


    fun insertScore(score: Score){
        scoreLocalSource.insert(score)
    }

    suspend fun getLatestScore(): Score? = scoreLocalSource.getLatestScore()

    fun delete(ids: List<Int>){
        scoreLocalSource.delete(scoreId = ids)
    }

    fun deleteAll(){
        scoreLocalSource.deleteAll()
    }
}