package com.vivekvishwanath.bitterskotlin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.repository.auth.AuthRepository
import com.vivekvishwanath.bitterskotlin.di.scope.AuthScope
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState
import com.vivekvishwanath.bitterskotlin.ui.auth.state.LoginFields
import com.vivekvishwanath.bitterskotlin.ui.auth.state.RegistrationFields
import com.vivekvishwanath.bitterskotlin.util.AbsentLiveData
import javax.inject.Inject

@AuthScope
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _stateEvent: MutableLiveData<AuthStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<AuthViewState> = MutableLiveData()

    val viewState: LiveData<AuthViewState>
        get() = _viewState


    val authState: LiveData<AuthState<FirebaseUser>> =
        Transformations
            .switchMap(_stateEvent) { stateEvent ->
                stateEvent?.let {
                    handleStateEvent(stateEvent)
                }
            }

    fun setStateEvent(event: AuthStateEvent) {
        _stateEvent.value = event
    }

    private fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<AuthState<FirebaseUser>> =
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

    private fun getCurrentViewStateOrNew(): AuthViewState {
        val value = viewState.value?.let {
            it
        }?: initNewViewState()
        return value
    }

    private fun initNewViewState(): AuthViewState = AuthViewState()

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) return
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setRegistraionFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) return
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun cancelJobs() {
        authRepository.cancelActiveJobs()
    }
}