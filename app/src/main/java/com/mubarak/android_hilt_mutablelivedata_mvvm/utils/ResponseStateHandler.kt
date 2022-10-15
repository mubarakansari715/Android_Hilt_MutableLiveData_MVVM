package com.mubarak.android_hilt_mutablelivedata_mvvm.utils

sealed class ResponseStateHandler<out T> {

    object OnLoading : ResponseStateHandler<Nothing>()

    class OnFailed(val code: Int, val message: String, val messageCode: String) :
        ResponseStateHandler<Nothing>()
    // according to response T is change either List<T> or only T
    class OnSuccessResponse<T>(val response: List<T>) : ResponseStateHandler<T>()
}