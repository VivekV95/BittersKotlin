package com.vivekvishwanath.bitterskotlin.ui.auth.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthViewModel
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.auth.state.RegistrationFields
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.util.performCrossFade
import com.vivekvishwanath.bitterskotlin.util.validateEmail
import com.vivekvishwanath.bitterskotlin.util.validatePassword
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_register.*
import java.lang.Exception
import javax.inject.Inject

class RegisterFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v) {
            register_button -> performRegistration()
        }
    }

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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, viewModelProviderFactory)[AuthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        subscribeObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        register_button.setOnClickListener(this)
    }

    private fun subscribeObservers() {
        viewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            if (authState is AuthState.Loading) {
                register_button.performCrossFade(true)
                register_progress_bar.performCrossFade(true)
            } else {
                register_button.performCrossFade(false)
                register_progress_bar.performCrossFade(false)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            register_email_edit_text.setText(viewState.registrationFields?.email)
            register_password_edit_text.setText(viewState.registrationFields?.password)
            register_password_confirm_edit_text.setText(viewState.registrationFields?.confirmPassword)
        })
    }

    private fun performRegistration() {
        when {
            !register_email_edit_text.validateEmail()
                    || !register_password_edit_text.validatePassword() -> return

            register_password_edit_text.text.toString()
                    != register_password_confirm_edit_text.text.toString() -> {
                register_password_edit_text.error = "Passwords must match"
                register_password_confirm_edit_text.error = "Passwords must match"
            }
            else -> viewModel.setStateEvent(
                AuthStateEvent.RegistrationEvent(
                    register_email_edit_text.text.toString(),
                    register_password_edit_text.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistraionFields(
            RegistrationFields(
                register_email_edit_text.text.toString(),
                register_password_edit_text.text.toString(),
                register_password_confirm_edit_text.text.toString()
            )
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJob()
    }
}
