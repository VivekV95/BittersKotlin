package com.vivekvishwanath.bitterskotlin.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_login.*


fun MaterialButton.performCrossFade(isLoading: Boolean) {
    if (isLoading)
        this.animate()
            .alpha(0f)
            .setDuration(200)
            .setListener(null)
    else
        this.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(200)
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
                .setDuration(200)
                .setListener(null)
        }
    else {
        val progressBar = this
        this.animate()
            .alpha(0f)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    progressBar.visibility = View.GONE
                }
            })
    }
}
