package com.example.kabaddikounter.repository

import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.datasource.ScoreLocalSource
import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreLocalSource: ScoreLocalSource) {

    val allScore: Flow<List<Score>> = scoreLocalSource.getAllScore()

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