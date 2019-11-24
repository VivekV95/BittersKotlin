package com.vivekvishwanath.bitterskotlin.ui

import com.vivekvishwanath.bitterskotlin.util.DataState

interface DataStateChangedListener {

    fun onDataStateChanged(dataState: DataState<*>)
}