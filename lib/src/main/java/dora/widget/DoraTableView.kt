package dora.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dora.widget.tableview.R
import dora.widget.tableview.TableCell

/**
 * 可横向或纵向滚动的表格控件，每格样式独立，并支持网格分割线（decoration）。
 */
class DoraTableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var orientationMode = VERTICAL
    private var manualSpanCount = 0

    // 分割线颜色 & 大小（px）
    private var dividerColor: Int = 0xFFE0E0E0.toInt()
    private var dividerSize: Int = 1
    private var dividerDecoration: ItemDecoration? = null
    private var tableAdapter = TableAdapter()

    init {
        attrs?.let {
            val ta: TypedArray = context.obtainStyledAttributes(it, R.styleable.DoraTableView)
            orientationMode = ta.getInt(R.styleable.DoraTableView_dview_tv_orientation, orientationMode)
            manualSpanCount = ta.getInt(R.styleable.DoraTableView_dview_tv_spanCount, 0)
            dividerColor = ta.getColor(
                R.styleable.DoraTableView_dview_tv_dividerColor,
                dividerColor
            )
            dividerSize = ta.getDimensionPixelSize(
                R.styleable.DoraTableView_dview_tv_dividerSize,
                dividerSize
            )
            ta.recycle()
        }
        adapter = tableAdapter
        setHasFixedSize(true)
    }

    /**
     * 设置二维表格数据。
     *
     * @param data 二维列表：List<行, List<TableCell>>
     */
    fun setData(data: List<List<TableCell>>) {
        val spanCount = if (manualSpanCount > 0) {
            manualSpanCount
        } else {
            data.maxOfOrNull { it.size } ?: 1  // 不论横向竖向，spanCount 始终是一行多少列
        }
        layoutManager = GridLayoutManager(context, spanCount, if (orientationMode == HORIZONTAL)
            VERTICAL else HORIZONTAL, false)
        dividerDecoration?.let { removeItemDecoration(it) }
        dividerDecoration = GridDividerItemDecoration(spanCount, dividerSize, dividerColor)
        addItemDecoration(dividerDecoration!!)
        val flat = mutableListOf<TableCell>()
        data.forEach { row ->
            repeat(spanCount) { i ->
                flat.add(row.getOrNull(i) ?: TableCell(""))
            }
        }
        tableAdapter.updateData(flat)
    }

    /**
     * 设置扁平化数据并指定每行单元格数。
     */
    fun setData(flatList: List<TableCell>, itemsPerRow: Int) {
        if (itemsPerRow <= 0) return setData(listOf())
        setData(flatList.chunked(itemsPerRow))
    }

    /**
     * 设置扁平化数组数据并指定每行单元格数。
     */
    fun setData(flatArray: Array<TableCell>, itemsPerRow: Int) {
        setData(flatArray.toList(), itemsPerRow)
    }

    private inner class TableAdapter : Adapter<TableAdapter.VH>() {

        private val items = mutableListOf<TableCell>()

        fun updateData(newList: List<TableCell>) {
            items.clear(); items.addAll(newList); notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val tv = TextView(parent.context).apply {
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER
            }
            return VH(tv)
        }

        override fun getItemCount(): Int {
            return items.count()
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val cell = items[position]
            holder.tv.text = cell.text
            holder.tv.textSize = cell.textSizeSp
            holder.tv.setTextColor(cell.textColor)
            if (cell.isBold) {
                holder.tv.setTypeface(holder.tv.typeface, android.graphics.Typeface.BOLD)
            } else {
                holder.tv.setTypeface(holder.tv.typeface, android.graphics.Typeface.NORMAL)
            }
            if (cell.isItalic) {
                holder.tv.setTypeface(holder.tv.typeface, android.graphics.Typeface.ITALIC)
            }
            val pad = (cell.paddingDp * holder.tv.resources.displayMetrics.density).toInt()
            holder.tv.setPadding(pad, pad, pad, pad)
            holder.tv.setBackgroundColor(cell.backgroundColor)
        }

        inner class VH(val tv: TextView) : ViewHolder(tv)
    }

    private class GridDividerItemDecoration(
        private val spanCount: Int,
        private val dividerSize: Int,
        private val dividerColor: Int
    ) : ItemDecoration() {

        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = dividerColor
        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: State
        ) {
            val position = (view.layoutParams as LayoutParams).viewAdapterPosition
            val column = position % spanCount
            outRect.right = if (column < spanCount - 1) dividerSize else 0
            outRect.bottom = dividerSize
        }

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: State) {
            val childCount = parent.childCount
            val itemCount = parent.adapter?.itemCount ?: 0
            val totalRowCount = if (itemCount % spanCount == 0) {
                itemCount / spanCount
            } else {
                itemCount / spanCount + 1
            }

            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as LayoutParams
                val position = parent.getChildAdapterPosition(child)
                if (position == RecyclerView.NO_POSITION) continue

                val column = position % spanCount
                val row = position / spanCount

                // ===== 外边框 =====
                // 左边框（第一列）
                if (column == 0) {
                    val left = child.left - params.leftMargin - dividerSize
                    val right = left + dividerSize
                    canvas.drawRect(
                        left.toFloat(),
                        (child.top - params.topMargin).toFloat(),
                        right.toFloat(),
                        (child.bottom + params.bottomMargin + dividerSize).toFloat(),
                        paint
                    )
                }
                // 上边框（第一行）
                if (row == 0) {
                    val top = child.top - params.topMargin - dividerSize
                    val bottom = top + dividerSize
                    canvas.drawRect(
                        (child.left - params.leftMargin).toFloat(),
                        top.toFloat(),
                        (child.right + params.rightMargin + dividerSize).toFloat(),
                        bottom.toFloat(),
                        paint
                    )
                }
                // 右边框（最后一列）
                if (column == spanCount - 1 || position == itemCount - 1) {
                    val left = child.right + params.rightMargin
                    val right = left + dividerSize
                    canvas.drawRect(
                        left.toFloat(),
                        (child.top - params.topMargin).toFloat(),
                        right.toFloat(),
                        (child.bottom + params.bottomMargin + dividerSize).toFloat(),
                        paint
                    )
                }
                // 下边框（最后一行）
                if (row == totalRowCount - 1) {
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + dividerSize
                    canvas.drawRect(
                        (child.left - params.leftMargin).toFloat(),
                        top.toFloat(),
                        (child.right + params.rightMargin + dividerSize).toFloat(),
                        bottom.toFloat(),
                        paint
                    )
                }

                // ===== 内部分割线 =====
                // 右边分割线（不是最后一列才画）
                if (column < spanCount - 1 && position < itemCount - 1) {
                    val left = child.right + params.rightMargin
                    val right = left + dividerSize
                    canvas.drawRect(
                        left.toFloat(),
                        (child.top - params.topMargin).toFloat(),
                        right.toFloat(),
                        (child.bottom + params.bottomMargin + dividerSize).toFloat(),
                        paint
                    )
                }
                // 下边分割线（不是最后一行才画）
                if (row < totalRowCount - 1) {
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + dividerSize
                    canvas.drawRect(
                        (child.left - params.leftMargin).toFloat(),
                        top.toFloat(),
                        (child.right + params.rightMargin + dividerSize).toFloat(),
                        bottom.toFloat(),
                        paint
                    )
                }
            }
        }
    }
}
