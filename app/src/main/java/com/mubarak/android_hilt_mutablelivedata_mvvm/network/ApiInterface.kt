package com.mubarak.android_hilt_mutablelivedata_mvvm.network

import com.mubarak.android_hilt_mutablelivedata_mvvm.home.model.HomeDataClass
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface ApiInterface {

    @GET("photos")
    suspend fun getPostData(): List<HomeDataClass>


}