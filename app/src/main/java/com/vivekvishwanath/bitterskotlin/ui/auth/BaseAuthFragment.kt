package com.vivekvishwanath.bitterskotlin.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.vivekvishwanath.bitterskotlin.util.LOG_TAG
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import java.lang.Exception
import javax.inject.Inject

abstract class BaseAuthFragment : Fragment() {


    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    lateinit var stateChangeListener: AuthStateChangedListener

    protected lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory)[AuthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        cancelActiveJobs()
    }

    protected fun cancelActiveJobs() {
        viewModel.cancelJobs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            stateChangeListener = context as AuthStateChangedListener
        } catch (e: ClassCastException) {
            Log.d(LOG_TAG, "${this.javaClass.simpleName}: $context must implement AuthStateChangedListener")
        }
    }

}