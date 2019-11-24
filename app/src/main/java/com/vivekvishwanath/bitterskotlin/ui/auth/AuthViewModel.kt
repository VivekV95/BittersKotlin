package com.vivekvishwanath.bitterskotlin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.repository.auth.AuthRepository
import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState
import com.vivekvishwanath.bitterskotlin.util.AbsentLiveData
import javax.inject.Inject

@AuthScope
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _stateEvent: MutableLiveData<AuthStateEvent> = MutableLiveData()

    val authState: LiveData<AuthState<AuthViewState>> =
        Transformations
            .switchMap(_stateEvent) { stateEvent ->
                stateEvent?.let {
                    handleStateEvent(stateEvent)
                }
            }

    fun setStateEvent(event: AuthStateEvent) {
        _stateEvent.value = event
    }

    private fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<AuthState<AuthViewState>> =
        when (stateEvent) {
            is AuthStateEvent.RegistrationEvent -> {
                authRepository.registerAccount(stateEvent.email, stateEvent.password)
            }
            is AuthStateEvent.LoginEvent -> {
                authRepository.signIn(stateEvent.email, stateEvent.password, false)
            }
            is AuthStateEvent.None -> {
                AbsentLiveData.create()
            }
            is AuthStateEvent.LoginOnReturnEvent -> {
                authRepository.setSignedInUser()
            }
        }
  
    fun initNewViewState(): AuthViewState = AuthViewState()

    fun cancelJob() {
        authRepository.cancelJob()
    }
}