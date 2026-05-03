package com.example.kabaddikounter.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import com.example.kabaddikounter.data.entities.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score ORDER BY teamId DESC")
    fun getAll() : Flow<List<Score>>

    @Query("SELECT * FROM score ORDER BY teamId DESC LIMIT 1")
    suspend fun getLatestScore() : Score?

    @Query("SELECT * FROM score WHERE teamId IN (:id)")
    fun loadAllByIds(id: IntArray): List<Score>

    @Query("SELECT * FROM score WHERE teamA_name LIKE :teamname LIMIT 1")
    fun findByName(teamname: String): Score

    @Insert
    fun insertAll(vararg scores: Score)

    @Query("DELETE FROM score WHERE teamId IN (:scoreId)")
    fun delete(scoreId: List<Int>)

    @Query("DELETE FROM score")
    fun deleteAll()
}