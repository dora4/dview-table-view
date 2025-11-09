package dora.widget.tableview

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class GridLayoutManager(context: Context, columnNumber: Int, isScrollEnabled: Boolean) :
    GridLayoutManager(context, columnNumber) {

    private var isScrollEnabled = true

    init {
        this.isScrollEnabled = isScrollEnabled
    }

    override fun canScrollVertically(): Boolean {
        // Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically()
    }

    override fun canScrollHorizontally(): Boolean {
        // Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollHorizontally()
    }
}