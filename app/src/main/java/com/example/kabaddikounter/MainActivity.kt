package com.example.kabaddikounter

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.database.AppDatabase
import com.example.kabaddikounter.databinding.ActivityMainBinding
import com.example.kabaddikounter.datasource.ScoreLocalSource
import com.example.kabaddikounter.repository.ScoreRepository
import com.example.kabaddikounter.ui.ScoreAdapter
import com.example.kabaddikounter.viewModels.ScoreViewModel
import com.example.kabaddikounter.viewModels.ViewModelFactory


class MainActivity : AppCompatActivity() {

//    val viewModel: ScoreViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var database : AppDatabase
    private lateinit var localSource: ScoreLocalSource
    private lateinit var repository: ScoreRepository
    private lateinit var scoreAdapter: ScoreAdapter

    val viewModel: ScoreViewModel by viewModels() {
        ViewModelFactory(getSharedPreferences("DarkMode", Context.MODE_PRIVATE),
            (application as MyApplication).scoreRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val scoreAdapter = ScoreAdapter(this)

        binding.scoreRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = scoreAdapter
        }

        viewModel.allScore.observe(this){
            scores -> scoreAdapter.submitList(scores)
        }

        sharedPreferences = getSharedPreferences("DarkMode", Context.MODE_PRIVATE)

        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

        binding.switchBtn.isChecked = isDarkMode

        if(isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        isStoragePermissionGranted()

        viewModel.toastMessage.observe(this) {
            message -> message?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            viewModel.onToastShown()

        }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        return if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
            false
        }
    }



}