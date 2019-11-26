package com.vivekvishwanath.bitterskotlin.ui

import com.vivekvishwanath.bitterskotlin.ui.main.DataState

interface DataStateChangedListener {

    fun onDataStateChanged(dataState: DataState<*>?)

    fun hideSoftKeyboard()
}