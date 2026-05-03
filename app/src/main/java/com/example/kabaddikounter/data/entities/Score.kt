package com.example.kabaddikounter.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val teamId: Int,
    @ColumnInfo(name = "teamA_name") val teamAName: String?,
    @ColumnInfo(name = "teamB_name") val teamBName: String?,
    @ColumnInfo(name = "teamA_score") val teamAScore: Int?,
    @ColumnInfo(name = "teamB_score") val teamBScore: Int?,
    @ColumnInfo(name = "timestamp") val timestamp: String?
)

