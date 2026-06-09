package com.example.kabaddikounter.viewModels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kabaddikounter.MyApplication
import com.example.kabaddikounter.repository.ScoreRepository

class SharedViewModelFactory(val application: Application, val scoreRepository: ScoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(application, scoreRepository) as T
    }
}