package com.vivekvishwanath.bitterskotlin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.auth.AuthRepository
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState
import com.vivekvishwanath.bitterskotlin.util.AbsentLiveData
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) {

    private val _stateEvent: MutableLiveData<AuthStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<AuthViewState> = MutableLiveData()

    val viewState: LiveData<AuthViewState>
        get() = _viewState

    val dataState: LiveData<AuthState<AuthViewState>> =
        Transformations
            .switchMap(_stateEvent) { stateEvent ->
                stateEvent?.let {
                    handleStateEvent(stateEvent)
                }
            }

    fun setStateEvent(event: AuthStateEvent) {
        _stateEvent.value = event
    }

    fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<AuthState<AuthViewState>> =
        when (stateEvent) {
            is AuthStateEvent.RegistrationEvent -> {
                authRepository.registerAccount(stateEvent.email, stateEvent.password)
            }
            is AuthStateEvent.LoginEvent -> {
                authRepository.signIn(stateEvent.email, stateEvent.password)
            }
            is AuthStateEvent.None -> {
                AbsentLiveData.create()
            }
        }

    fun initNewViewState(): AuthViewState = AuthViewState()
}