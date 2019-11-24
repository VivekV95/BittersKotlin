package com.vivekvishwanath.bitterskotlin.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


fun MaterialButton.performCrossFade(isLoading: Boolean) {
    if (isLoading)
        this.animate()
            .alpha(0f)
            .setDuration(CROSS_FADE_DURATION)
            .setListener(null)
    else
        this.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(CROSS_FADE_DURATION)
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
                .setDuration(CROSS_FADE_DURATION)
                .setListener(null)
        }
    else {
        val progressBar = this
        this.animate()
            .alpha(0f)
            .setDuration(CROSS_FADE_DURATION)
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
