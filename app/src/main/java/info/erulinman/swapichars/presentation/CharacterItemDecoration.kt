package info.erulinman.swapichars.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CharacterItemDecoration(private val dimenId: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val px = parent.resources.getDimensionPixelSize(dimenId)
        outRect.apply {
            top = px
            parent.adapter?.let adapter@{ adapter ->
                val childAdapterPosition = parent.getChildAdapterPosition(view)
                    .let { if (it == RecyclerView.NO_POSITION) return@adapter else it }
                if (childAdapterPosition == adapter.itemCount - 1)
                    bottom = px
            }
            right = px
            left = px
        }
    }
}