package com.example.kabaddikounter.ui.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager

class SettingsViewModel : ViewModel() {
    fun toggleDarkMode(enabled: Boolean) {
        Log.d("darkMode", "$enabled")
        if(enabled) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}