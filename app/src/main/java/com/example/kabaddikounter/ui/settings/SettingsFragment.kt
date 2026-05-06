package com.example.kabaddikounter.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.kabaddikounter.R


class SettingsFragment: PreferenceFragmentCompat() {
    private val settingsViewModel : SettingsViewModel by viewModels()
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootkey: String?
    ) {
//        val settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        setPreferencesFromResource(R.xml.fragment_settings, rootkey)
        Log.d("SettingsDebug", "onCreatePreferences called")

        val darkModePref: SwitchPreferenceCompat? = findPreference("prefs_dark_mode")

        if(darkModePref==null) Log.e("SettingsDebug", "Could not find preference")
        else Log.d("SettingsDebug", "Preference found!")

        darkModePref?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener {
            _, newValue ->  val isEnabled = newValue as Boolean;
            Log.d("SettingsDebug", "Listener triggered!")
            settingsViewModel.toggleDarkMode(isEnabled);
            true
        }
    }
}