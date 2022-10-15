package com.mubarak.android_hilt_mutablelivedata_mvvm.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.model.HomeDataClass
import com.mubarak.android_hilt_mutablelivedata_mvvm.home.repository.HomeRepository
import com.mubarak.android_hilt_mutablelivedata_mvvm.utils.ResponseStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: HomeRepository) : ViewModel() {

    val homePostDataResponse = MutableLiveData<ResponseStateHandler<HomeDataClass>?>()

    fun homePostDataApiCall() = viewModelScope.launch {
        homePostDataResponse.value = ResponseStateHandler.OnLoading
        homePostDataResponse.value = ResponseStateHandler.OnSuccessResponse(repository.getPostData())
    }
}