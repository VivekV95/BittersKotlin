package com.vivekvishwanath.bitterskotlin.ui.auth.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthViewModel
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AuthActivity).authComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it, viewModelProviderFactory)
                .get(AuthViewModel::class.java)
        }

        login_button.setOnClickListener {
            performLogin(
                login_email_edit_text.text.toString(),
                login_password_edit_text.text.toString())
        }

        subscribeObservers()
    }

    fun performLogin(email: String, password: String) {
        viewModel.setStateEvent(AuthStateEvent.LoginEvent(email, password))
    }

    private fun subscribeObservers() {
        viewModel.authState.observe(viewLifecycleOwner, Observer {authState ->
            when (authState) {
                is AuthState.Loading -> {
                    val i = 0
                }
                is AuthState.Authenticated -> {
                    val i = 0
                }
                is AuthState.Error -> {
                    val i = 0
                }
                is AuthState.NotAuthenticated -> {
                    val i = 0
                }
            }
        })
    }

}
