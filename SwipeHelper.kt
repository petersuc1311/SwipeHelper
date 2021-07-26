package dev.psuchanek.jonsfueltracker_v_1_1.utils

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

const val DEFAULT_BACKGROUND_CORNER_OFFSET: Int = 20
const val DEFAULT_BOUNDS_VALUE = 0
const val DEFAULT_DRAG_DIRECTION = 0

inline fun itemTouchHelper(
    backgroundCornerOffset: Int = DEFAULT_BACKGROUND_CORNER_OFFSET,
    crossinline iconDrawable: () -> Drawable,
    crossinline colorDrawable: () -> ColorDrawable,
    crossinline onSwipeRefresherHandling: (actionState: Int, isCurrentlyActive: Boolean) -> Unit,
    crossinline onSwipe: (position: Int) -> Unit
) = object : ItemTouchHelper.SimpleCallback(
    DEFAULT_DRAG_DIRECTION, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
) {
    private val background: ColorDrawable = colorDrawable()
    private var actionIcon = iconDrawable()

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        onSwipeRefresherHandling(actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - actionIcon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - actionIcon.intrinsicHeight) / 2
        val iconBottom = iconTop + actionIcon.intrinsicHeight

        drawChild(dX, itemView, iconMargin, iconTop, iconBottom)
        background.draw(c)
        actionIcon.draw(c)
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe(viewHolder.layoutPosition)
    }


    private fun drawChild(
        dX: Float,
        itemView: View,
        iconMargin: Int,
        iconTop: Int,
        iconBottom: Int
    ) {
        when {
            dX > DEFAULT_BOUNDS_VALUE -> {
                val iconLeft = itemView.left + iconMargin + actionIcon.intrinsicWidth
                val iconRight = itemView.left + iconMargin
                actionIcon.setBounds(iconRight, iconTop, iconLeft, iconBottom)

                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + (dX.toInt() + backgroundCornerOffset),
                    itemView.bottom
                )
            }
            dX < DEFAULT_BOUNDS_VALUE -> {
                val iconLeft = itemView.right - iconMargin - actionIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                actionIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(
                    itemView.right + (dX.toInt() - backgroundCornerOffset),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            else -> background.setBounds(
                DEFAULT_BOUNDS_VALUE,
                DEFAULT_BOUNDS_VALUE,
                DEFAULT_BOUNDS_VALUE,
                DEFAULT_BOUNDS_VALUE
            )
        }
    }
}