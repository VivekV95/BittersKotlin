package com.vivekvishwanath.bitterskotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vivekvishwanath.bitterskotlin.ui.main.DataState

/**
 * Taken from the CodingWithMitch's OpenAPI app at
 * https://github.com/mitchtabian/Open-API-Android-App/tree/master/app/src/main/java/com/codingwithmitch/openapi
 */
abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {

    private val _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()

    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> =
        Transformations
            .switchMap(_stateEvent) { stateEvent ->
                stateEvent?.let {
                    handleStateEvent(stateEvent)
                }
            }

    fun setStateEvent(event: StateEvent) {
        _stateEvent.value = event
    }

    fun getCurrentViewStateOrNew(): ViewState {
        val value = viewState.value?.let { 
            it
        }?: initNewViewState()
        return value
    }

    abstract fun initNewViewState(): ViewState

    abstract fun handleStateEvent(stateEvent: StateEvent): LiveData<DataState<ViewState>>
}
