package com.example.veggieyou.Common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EspacosDosItems(internal var espaco: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = espaco
        outRect.left = espaco
        outRect.bottom = espaco
        outRect.top = espaco
    }
}