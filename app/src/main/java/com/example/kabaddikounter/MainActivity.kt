package com.example.kabaddikounter

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabaddikounter.data.live.LiveMatch
import com.example.kabaddikounter.databinding.ActivityMainBinding
import com.example.kabaddikounter.ui.ScoreAdapter
import com.example.kabaddikounter.ui.live.LiveMatchAdapter
import com.example.kabaddikounter.service.LiveScoreForegroundService
import com.example.kabaddikounter.viewModels.LiveMatchViewModel
import com.example.kabaddikounter.viewModels.LiveMatchViewModelFactory
import com.example.kabaddikounter.viewModels.ScoreViewModel
import com.example.kabaddikounter.viewModels.ViewModelFactory
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var scoreAdapter: ScoreAdapter
    private lateinit var liveMatchAdapter: LiveMatchAdapter

    private val scoreViewModel: ScoreViewModel by viewModels() {
        ViewModelFactory(application, (application as MyApplication).scoreRepository)
    }

    private val liveMatchViewModel: LiveMatchViewModel by viewModels() {
        LiveMatchViewModelFactory((application as MyApplication).liveMatchRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = scoreViewModel
        binding.lifecycleOwner = this

        scoreAdapter = ScoreAdapter(this)
        liveMatchAdapter = LiveMatchAdapter { match -> liveMatchViewModel.subscribe(match) }

        binding.scoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = scoreAdapter
            isNestedScrollingEnabled = false
        }

        binding.liveMatchRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = liveMatchAdapter
            isNestedScrollingEnabled = false
        }

        binding.refreshButton.setOnClickListener { liveMatchViewModel.refreshMatches() }
        binding.startForegroundButton.setOnClickListener { LiveScoreForegroundService.start(this) }
        binding.stopForegroundButton.setOnClickListener { LiveScoreForegroundService.stop(this) }
        binding.buttonReset.setOnClickListener {
            (application as MyApplication).liveMatchRepository.clearActiveMatch()
            LiveScoreForegroundService.stop(this)
            scoreViewModel.reset()
            liveMatchViewModel.refreshMatches()
            Toast.makeText(this, getString(R.string.subscription_reset), Toast.LENGTH_SHORT).show()
        }

        binding.darkModeSwitch.isChecked = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.pref_key_dark_mode), false)
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateDarkMode(isChecked)
        }

        scoreViewModel.allScore.observe(this) { scores ->
            scoreAdapter.submitList(scores)
        }

        scoreViewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                scoreViewModel.onToastShown()
            }
        }

        scoreViewModel.liveMatchLocked.observe(this) { locked ->
            setCounterEditable(!locked)
        }

        liveMatchViewModel.matches.observe(this) { matches ->
            liveMatchAdapter.submitList(matches)
            renderEmptyState(matches)
        }

        liveMatchViewModel.activeMatch.observe(this) { activeMatch ->
            renderActiveMatch(activeMatch)
            liveMatchAdapter.setActiveMatchId(activeMatch?.id)
            scoreViewModel.applyLiveMatch(activeMatch)
        }

        liveMatchViewModel.loading.observe(this) { loading ->
            binding.loadingIndicator.isVisible = loading
        }

        liveMatchViewModel.errorMessage.observe(this) { error ->
            binding.errorText.isVisible = !error.isNullOrBlank()
            binding.errorText.text = error
        }

        liveMatchViewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                liveMatchViewModel.clearToast()
            }
        }

        requestNotificationPermissionIfNeeded()

        liveMatchViewModel.refreshMatches()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 2)
            }
        }
    }

    private fun updateDarkMode(isDarkMode: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putBoolean(getString(R.string.pref_key_dark_mode), isDarkMode)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setCounterEditable(isEditable: Boolean) {
        binding.textView.isEnabled = isEditable
        binding.textView2.isEnabled = isEditable
        binding.buttonTeamA1.isEnabled = isEditable
        binding.buttonTeamA2.isEnabled = isEditable
        binding.buttonTeamB1.isEnabled = isEditable
        binding.buttonTeamB2.isEnabled = isEditable
        binding.counterLockedHint.isVisible = !isEditable
    }

    private fun renderActiveMatch(match: LiveMatch?) {
        binding.activeMatchCard.isVisible = match != null
        binding.startForegroundButton.isEnabled = match != null
        binding.stopForegroundButton.isEnabled = match != null
        if (match == null) {
            binding.activeMatchTeamText.text = getString(R.string.live_match_no_active)
            binding.activeMatchScoreText.text = ""
            binding.activeMatchStatusText.text = ""
            return
        }

        binding.activeMatchTeamText.text = match.title
        binding.activeMatchScoreText.text = match.scoreLine
        binding.activeMatchStatusText.text = match.status.displayName
    }

    private fun renderEmptyState(matches: List<LiveMatch>) {
        val isEmpty = matches.isEmpty() && !binding.loadingIndicator.isVisible
        binding.noMatchesText.isVisible = isEmpty
    }


}