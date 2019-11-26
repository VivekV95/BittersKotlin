package com.vivekvishwanath.bitterskotlin.ui.auth.fragment


import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthState
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthViewModel
import com.vivekvishwanath.bitterskotlin.ui.auth.BaseAuthFragment
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.ui.auth.state.LoginFields
import com.vivekvishwanath.bitterskotlin.ui.performCrossFade
import com.vivekvishwanath.bitterskotlin.ui.validateEmail
import com.vivekvishwanath.bitterskotlin.ui.validatePassword
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception
import javax.inject.Inject

class LoginFragment : BaseAuthFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v) {
            login_button -> performLogin()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity as AuthActivity).authComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        setupOnClickListeners()
    }

    private fun setupOnClickListeners() {
        setupRegistrationClick()
        login_button.setOnClickListener(this)
    }

    private fun setupRegistrationClick() {
        val spannableString =
            SpannableString(resources.getString(R.string.don_t_have_an_account_register_in_seconds))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
        spannableString.setSpan(clickableSpan, 23, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        register_text.text = spannableString
        register_text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun subscribeObservers() {
        viewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            if (authState is AuthState.Loading) {
                stateChangeListener.onAuthStateChanged(authState)
                login_button.performCrossFade(true)
                login_progress_bar.performCrossFade(true)
            } else {
                login_button.performCrossFade(false)
                login_progress_bar.performCrossFade(false)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            login_email_edit_text.setText(viewState.loginFields?.email)
            login_password_edit_text.setText(viewState.loginFields?.password)
        })
    }

    private fun performLogin() {
        when {
            !login_email_edit_text.validateEmail() ||
                    !login_password_edit_text.validatePassword() -> return
            else -> viewModel.setStateEvent(
                AuthStateEvent.LoginEvent(
                    login_email_edit_text.text.toString(),
                    login_password_edit_text.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                login_email_edit_text.text.toString(),
                login_password_edit_text.text.toString()
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelJobs()
    }
}
