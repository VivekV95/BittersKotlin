package com.vivekvishwanath.bitterskotlin.ui.auth.fragment


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.vivekvishwanath.bitterskotlin.R
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthActivity
import com.vivekvishwanath.bitterskotlin.ui.auth.AuthViewModel
import com.vivekvishwanath.bitterskotlin.ui.auth.state.AuthStateEvent
import com.vivekvishwanath.bitterskotlin.util.AuthState
import com.vivekvishwanath.bitterskotlin.viewmodel.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment() {

    lateinit var navController: NavController
    var shortAnimationDuration: Int = 0

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

        navController = Navigation.findNavController(view)

        activity?.let {
            viewModel = ViewModelProvider(it, viewModelProviderFactory)
                .get(AuthViewModel::class.java)
        }

        login_button.setOnClickListener {
            performLogin(
                login_email_edit_text.text.toString(),
                login_password_edit_text.text.toString()
            )
        }

        subscribeObservers()
        setupRegistrationClick()
    }

    override fun onStart() {
        super.onStart()
        viewModel.setStateEvent(AuthStateEvent.LoginOnReturnEvent)
    }

    private fun setupRegistrationClick() {
        val spannableString = SpannableString(resources.getString(R.string.don_t_have_an_account_register_in_seconds))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(activity, "Registration clicked", Toast.LENGTH_SHORT).show()
            }
        }
        spannableString.setSpan(clickableSpan, 23, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        register_text.text = spannableString
        register_text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun subscribeObservers() {
        viewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    authState.data?.getContentIfNotHandled()?.let {
                        Toast.makeText(activity, "You're logged in!", Toast.LENGTH_SHORT).show()
                    }
                }
                is AuthState.Error -> {
                    authState.message?.getContentIfNotHandled()?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            if (authState is AuthState.Loading) {
                performCrossFade(true)
            } else {
                performCrossFade(false)
            }
        })
    }

    fun performLogin(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty())
            viewModel.setStateEvent(AuthStateEvent.LoginEvent(email, password))
    }

    fun performCrossFade(isLoading: Boolean) {
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        if (isLoading) {
            progress_bar.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setListener(null)
            }

            login_button.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {

                    }
                })

        } else {
            login_button.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration.toLong())
                    .setListener(null)
            }

            progress_bar.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progress_bar.visibility = View.GONE
                    }
                })
        }
    }

}
