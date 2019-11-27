package com.vivekvishwanath.bitterskotlin.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.vivekvishwanath.bitterskotlin.ui.DataStateChangedListener
import com.vivekvishwanath.bitterskotlin.ui.main.view.CocktailListViewModel
import com.vivekvishwanath.bitterskotlin.util.LOG_TAG
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import java.lang.ClassCastException
import java.lang.Exception
import javax.inject.Inject

abstract class BaseCocktailFragment: Fragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var viewModel: CocktailListViewModel

    lateinit var stateChangeListener: DataStateChangedListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory)[CocktailListViewModel::class.java]
        }?: throw Exception("Invalid Activity")

        cancelActiveJobs()
    }

    protected fun cancelActiveJobs() {
        viewModel.cancelActiveJobs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as DataStateChangedListener
        } catch (e: ClassCastException) {
            Log.d(LOG_TAG, "${this.javaClass.simpleName}: $context must implement DataStateChangeListener")
        }
    }
}