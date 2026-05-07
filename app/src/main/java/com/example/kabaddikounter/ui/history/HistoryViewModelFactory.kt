package com.example.kabaddikounter.ui.history

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kabaddikounter.repository.ScoreRepository

class HistoryViewModelFactory(private val scoreRepository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(scoreRepository) as T
    }


}