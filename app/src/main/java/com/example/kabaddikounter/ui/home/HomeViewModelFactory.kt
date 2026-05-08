package com.example.kabaddikounter.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kabaddikounter.repository.ScoreRepository

class HomeViewModelFactory(private val scoreRepository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(scoreRepository) as T
    }


}