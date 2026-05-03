package com.example.kabaddikounter.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kabaddikounter.repository.ScoreRepository

class ViewModelFactory(val sharedPreferences: SharedPreferences, val scoreRepository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScoreViewModel(sharedPreferences, scoreRepository) as T
    }
}