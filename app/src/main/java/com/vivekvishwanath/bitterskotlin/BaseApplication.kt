package com.vivekvishwanath.bitterskotlin

import android.app.Application
import com.vivekvishwanath.bitterskotlin.di.DaggerAppComponent
import java.util.concurrent.atomic.AtomicBoolean

class BaseApplication: Application() {

    val appComponent by lazy {
        DaggerAppComponent
            .builder()
            .application(this)
            .popularCall(AtomicBoolean(false))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
    }
}