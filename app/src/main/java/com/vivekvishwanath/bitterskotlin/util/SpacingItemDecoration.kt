package com.vivekvishwanath.bitterskotlin.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration (private val space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space * 2

        if (parent.getChildLayoutPosition(view) < 2)
            outRect.top = space
        else
            outRect.top = 0
    }
}