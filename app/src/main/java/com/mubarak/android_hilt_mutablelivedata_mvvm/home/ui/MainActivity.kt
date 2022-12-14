package com.mubarak.android_hilt_mutablelivedata_mvvm.home.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mubarak.android_hilt_mutablelivedata_mvvm.databinding.ActivityMainBinding
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.adapter.HomeAdapter
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.model.HomeDataClass
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.viewmodel.HomeViewModel
import com.mubarak.android_hilt_mutablelivedata_mvvm.utils.DebugLog
import com.mubarak.android_hilt_mutablelivedata_mvvm.utils.ResponseStateHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

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

                    DebugLog.e("OnSuccessResponse : ${it.response}")

                    recyclerviewList.adapter = HomeAdapter(it.response) {
                        Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
                    }

                }
                is ResponseStateHandler.OnFailed -> {
                    Log.d(TAG, "Failed to ${it.message}")
                }

            }
        }

    }
}