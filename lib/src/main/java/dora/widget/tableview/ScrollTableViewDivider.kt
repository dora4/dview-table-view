package dora.widget.tableview

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dora.widget.DoraScrollTableView

class ScrollTableViewDivider(private val tableView: DoraScrollTableView) :
    RecyclerView.ItemDecoration() {

    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val cellIsHeader: Boolean
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % tableView.columnCount // item column
        val container = view.findViewById<View>(R.id.container)
        val title = view.findViewById<View>(R.id.title) as TextView
        val border = dp2px(tableView.context, 1f)
        val padding = dp2px(tableView.context, tableView.padding.toFloat())
        container.setPadding(
            if (column != 0) 0 else border,
            if (position >= tableView.columnCount) 0 else border,
            border,
            border
        )
        cellIsHeader = if (tableView.headersOnTop) {
            column == 0
        } else {
            false
        }
        if (cellIsHeader) {
            container.setBackgroundColor(tableView.headerBordersColor)
            title.setBackgroundColor(tableView.headerBackgroundColor)
        } else {
            container.setBackgroundColor(tableView.dataBordersColor)
            title.setBackgroundColor(tableView.dataBackgroundColor)
        }
        title.setPadding(padding, padding, padding, padding)
    }
}