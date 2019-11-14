package com.vivekvishwanath.bitterskotlin.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vivekvishwanath.bitterskotlin.BaseApplication
import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.auth.fragment.LoginFragment

class AuthActivity : AppCompatActivity() {

    val authComponent by lazy {
        (application as BaseApplication)
            .appComponent
            .authComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        authComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}
