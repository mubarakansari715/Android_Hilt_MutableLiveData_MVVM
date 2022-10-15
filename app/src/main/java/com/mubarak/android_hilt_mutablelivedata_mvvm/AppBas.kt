package com.mubarak.android_hilt_mutablelivedata_mvvm

import android.app.Application
import com.mubarak.android_hilt_mutablelivedata_mvvm.utils.MyPreference
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AppBas:Application() {

    @Inject
    lateinit var mPreference: MyPreference

    companion object {
        private var instance: AppBas? = null
        fun applicationContext(): AppBas {
            return instance as AppBas
        }
    }

    init {
        instance = this
    }
}