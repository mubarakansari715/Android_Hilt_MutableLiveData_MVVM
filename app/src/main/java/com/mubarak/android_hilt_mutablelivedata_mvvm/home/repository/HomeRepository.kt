package com.mubarak.android_hilt_mutablelivedata_mvvm.home.repository

import com.mubarak.android_hilt_mutablelivedata_mvvm.home.model.HomeDataClass
import com.mubarak.android_hilt_mutablelivedata_mvvm.network.ApiInterface
import javax.inject.Inject

class HomeRepository @Inject constructor(val apiInterface: ApiInterface) {
    suspend fun getPostData(): List<HomeDataClass> {
        return apiInterface.getPostData()
    }
}