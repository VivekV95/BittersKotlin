package com.vivekvishwanath.bitterskotlin.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_login.*


fun MaterialButton.performCrossFade(isLoading: Boolean) {
    if (isLoading)
        this.animate()
            .alpha(0f)
            .setDuration(crossFadeDuration)
            .setListener(null)
    else
        this.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(crossFadeDuration)
                .setListener(null)
        }
}

fun ProgressBar.performCrossFade(isLoading: Boolean) {
    if (isLoading)
        this.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(crossFadeDuration)
                .setListener(null)
        }
    else {
        val progressBar = this
        this.animate()
            .alpha(0f)
            .setDuration(crossFadeDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    progressBar.visibility = View.GONE
                }
            })
    }
}

fun TextInputEditText.validateEmail(): Boolean {
    val email = this.text.toString()
    if (email.isBlank()) {
        this.error = "Email cannot be blank"
        return false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        this.error = "Email must have valid format"
        return false
    }
    return true
}

fun TextInputEditText.validatePassword(): Boolean {
    val password = this.text.toString()
    if (password.isBlank()) {
        this.error = "Password cannot be blank"
        return false
    } else if (password.length < 8 || password.length > 64) {
        this.error = "Password must be between 8 and 64 characters"
        return false
    }
    return true
}

fun TextInputLayout.validateEmail(email: String): Pair<Boolean, String?> {
    if (email.isBlank()) return Pair(false, "Email cannot be blank")
    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return Pair(false, "Email must have valid format")
    return Pair(true, null)
}

fun TextInputLayout.validatePassword(password: String): Pair<Boolean, String?> {
    if (password.isBlank()) return Pair(false, "Password cannot be blank")
    else if (password.length < 8 || password.length > 64)
        return Pair(false, "Password must be between 8 and 64 characters")
    return Pair(true, null)
}
