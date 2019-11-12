package com.vivekvishwanath.bitterskotlin.ui.auth

import androidx.lifecycle.LiveData
import com.vivekvishwanath.bitterskotlin.ui.BaseViewModel
import com.vivekvishwanath.bitterskotlin.ui.DataState
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthViewState

class AuthViewModel: BaseViewModel<AuthStateEvent, AuthViewState>() {

    override fun initNewViewState(): AuthViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}