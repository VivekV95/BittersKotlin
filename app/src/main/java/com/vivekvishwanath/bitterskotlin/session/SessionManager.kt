package com.vivekvishwanath.bitterskotlin.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.util.LOG_TAG
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val application: Application,
    private val mAuth: FirebaseAuth
){

    private val currentUser = MutableLiveData<AuthState<FirebaseUser>>()

    fun setCurrentUser(state: AuthState<FirebaseUser>) {
        GlobalScope.launch(Main) {
            currentUser.value = state
        }
    }

    fun logOut() {
        mAuth.signOut()
        currentUser.value = AuthState.NotAuthenticated()
    }

    fun getCurrentUser(): LiveData<AuthState<FirebaseUser>> {
        return currentUser
    }

    fun deleteCurrentUser() {
        currentUser.value?.data?.peekContent()?.delete()
    }

    fun isConnectedToTheInternet(): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            if (Build.VERSION.SDK_INT < 23) {
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo != null) {
                    return (networkInfo.isConnected &&
                            (networkInfo.type == ConnectivityManager.TYPE_WIFI
                                    || networkInfo.type == ConnectivityManager.TYPE_MOBILE))
                }
            } else {
                val network = connectivityManager.activeNetwork
                if (network != null) {
                    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                    networkCapabilities?.let {
                        return (networkCapabilities.hasTransport(
                            NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "is not connected to internet: ${e.message}")
        }
        return false
    }

}