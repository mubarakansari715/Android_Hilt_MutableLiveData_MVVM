package com.mubarak.android_hilt_mutablelivedata_mvvm.home.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mubarak.android_hilt_mutablelivedata_mvvm.databinding.ActivityMainBinding
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.model.HomeDataClass
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.viewmodel.HomeViewModel
import com.mubarak.android_hilt_mutablelivedata_mvvm.utils.ResponseStateHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val getBindingData get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        lifecycleScope.launchWhenStarted {
            homeViewModel.homePostDataApiCall()
        }
        homeViewModel.homePostDataResponse.observe(this) {
            if (it == null) {
                return@observe
            }
            when (it) {
                is ResponseStateHandler.OnLoading -> {
                    Log.d(TAG, "Loading")
                }
                is ResponseStateHandler.OnSuccessResponse<HomeDataClass> -> {
                    Log.d(TAG, "OnSuccessResponse : ${it.response} ")
                    getBindingData.txtId.text = it.response.toString()

                }
                is ResponseStateHandler.OnFailed -> {
                    Log.d(TAG, "Failed to ${it.message}")
                }

            }
        }

    }
}