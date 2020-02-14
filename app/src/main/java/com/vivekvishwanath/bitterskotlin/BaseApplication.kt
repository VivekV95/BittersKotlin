package com.vivekvishwanath.bitterskotlin

import android.app.Application
import com.vivekvishwanath.bitterskotlin.di.DaggerAppComponent

class BaseApplication: Application() {

    val appComponent by lazy {
        DaggerAppComponent
            .builder()
            .application(this)
            .popularCall(false)
            .build()
    }

}