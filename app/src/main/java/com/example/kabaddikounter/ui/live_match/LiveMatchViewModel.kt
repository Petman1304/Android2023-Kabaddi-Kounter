package com.example.kabaddikounter.ui.live_match

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kabaddikounter.ApiInterface
import com.example.kabaddikounter.RetrofitInst
import com.example.kabaddikounter.data.entities.Score
import com.example.kabaddikounter.viewModels.SharedViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import kotlin.getValue
import kotlin.time.Duration.Companion.seconds

class LiveMatchViewModel : ViewModel() {

    private lateinit var apiInterface: ApiInterface
    private val _matches = MutableLiveData<List<Score>>()
    val matches : LiveData<List<Score>> = _matches

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun loadMatches(){
        viewModelScope.launch {
            while(isActive){
                try {
                    apiInterface = RetrofitInst.getInstance().create(ApiInterface::class.java)
                    val res = apiInterface.getMatches()
                    if (res.isSuccessful)
                    {
                        _matches.value = res.body()
                    }

                } catch (e: Exception){
                    e.printStackTrace()
                }

                delay(Duration.ofSeconds(10))
            }

        }
    }
}