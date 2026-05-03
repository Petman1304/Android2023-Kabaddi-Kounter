package com.example.kabaddikounter.datasource

import com.example.kabaddikounter.dao.ScoreDao
import com.example.kabaddikounter.data.entities.Score
import kotlinx.coroutines.flow.Flow

class ScoreLocalSource(private val scoreDao: ScoreDao) {

    fun getAllScore(): Flow<List<Score>>{
        return scoreDao.getAll()
    }

    suspend fun getLatestScore() : Score?{
        return scoreDao.getLatestScore()
    }

    fun insert(score: Score){
        scoreDao.insertAll(score)
    }

    fun delete(scoreId: List<Int>){
        scoreDao.delete(scoreId)
    }

    fun deleteAll(){
        scoreDao.deleteAll()
    }
}