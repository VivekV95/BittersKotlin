package com.vivekvishwanath.bitterskotlin.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CocktailSpacingItemDecoration(
    private val space: Int,
    private val isPortrait: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space * 2

        if (isPortrait) {
            if (parent.getChildLayoutPosition(view) < 2)
                outRect.top = space * 4
            else
                outRect.top = 0
        } else {
            if (parent.getChildLayoutPosition(view) < 4)
                outRect.top = space * 4
            else
                outRect.top = 0
        }

    }
}