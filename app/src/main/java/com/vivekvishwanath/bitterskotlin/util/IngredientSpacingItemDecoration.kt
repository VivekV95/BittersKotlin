package com.vivekvishwanath.bitterskotlin.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class IngredientSpacingItemDecoration(
    private val space: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildLayoutPosition(view) < 1)
            outRect.top = space * 2
        else
            outRect.top = space
    }
}