package com.example.kabaddikounter

import android.Manifest
import android.content.Context
import android.content.Intent
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
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.database.AppDatabase
import com.example.kabaddikounter.databinding.ActivityMainBinding
import com.example.kabaddikounter.datasource.ScoreLocalSource
import com.example.kabaddikounter.repository.ScoreRepository
import com.example.kabaddikounter.service.FCMService
import com.example.kabaddikounter.service.LiveScoreService
import com.example.kabaddikounter.ui.ScoreAdapter
import com.example.kabaddikounter.viewModels.ScoreViewModel
import com.example.kabaddikounter.viewModels.SharedViewModel
import com.example.kabaddikounter.viewModels.ViewModelFactory
import com.example.kabaddikounter.viewModels.SharedViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging



class MainActivity : AppCompatActivity() {

//    val viewModel: ScoreViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var database : AppDatabase
    private lateinit var localSource: ScoreLocalSource
    private lateinit var repository: ScoreRepository
    private lateinit var scoreAdapter: ScoreAdapter

    private val sharedViewModel: SharedViewModel by viewModels {
        SharedViewModelFactory(
            (application as MyApplication).scoreRepository
        )
    }

    val viewModel: ScoreViewModel by viewModels {
        ViewModelFactory(getSharedPreferences("DarkMode", Context.MODE_PRIVATE),
            (application as MyApplication).scoreRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("prefs_dark_mode", false)

        if(isDarkMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_live_match, R.id.navigation_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        isStoragePermissionGranted()
        isNotificationPermissionGranted()

        subscribeTopic(this, "testtopic")

//
//        viewModel.toastMessage.observe(this) {
//            message -> message?.let{
//                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//            viewModel.onToastShown()
//
//        }
//        }
//
//        Intent(applicationContext, LiveScoreService::class.java).also {
//            it.action = LiveScoreService.Action.START.toString()
//            startService(it)
//        }
//    }
//

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

    private fun isNotificationPermissionGranted(): Boolean {
        val permission = Manifest.permission.POST_NOTIFICATIONS

        return  if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
            true
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
            false
        }
    }

    fun subscribeTopic(context: Context, topic: String){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            Toast.makeText(context, "Subscribed to $topic", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to subscribe to $topic", Toast.LENGTH_SHORT).show()
        }
    }

    fun unsubscribeTopic(context: Context, topic:String){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener {
            Toast.makeText(context, "Unsubscribed to $topic", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to unsubscribed to $topic", Toast.LENGTH_SHORT).show()
        }
    }


}

