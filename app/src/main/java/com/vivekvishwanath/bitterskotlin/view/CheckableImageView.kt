package com.vivekvishwanath.bitterskotlin.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.ImageView


class CheckableImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs),
    Checkable, View.OnClickListener {
    private var mChecked: Boolean = false

    init {
        setOnClickListener(this)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked)
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        return drawableState
    }

    override fun toggle() {
        isChecked = !mChecked
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked == checked)
            return
        mChecked = checked
        refreshDrawableState()
    }

    override fun onClick(v: View) {
        toggle()
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}