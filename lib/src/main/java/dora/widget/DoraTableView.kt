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

    private val tableAdapter = TableAdapter()

    init {
        attrs?.let {
            val ta: TypedArray = context.obtainStyledAttributes(it, R.styleable.DoraTableView)
            orientationMode = ta.getInt(R.styleable.DoraTableView_dview_tv_orientation, VERTICAL)
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
        val spanCount = if (manualSpanCount > 0) manualSpanCount else
            if (orientationMode == VERTICAL) data.maxOfOrNull { it.size } ?: 1 else data.size.coerceAtLeast(1)
        layoutManager = GridLayoutManager(context, spanCount, orientationMode, false)
        dividerDecoration?.let { removeItemDecoration(it) }
        dividerDecoration = GridDividerItemDecoration(spanCount, dividerSize, dividerColor)
        addItemDecoration(dividerDecoration!!)
        val flat = mutableListOf<TableCell>()
        if (orientationMode == VERTICAL) {
            data.forEach { row ->
                repeat(spanCount) { i -> flat.add(row.getOrNull(i) ?: TableCell("")) }
            }
        } else {
            for (i in 0 until spanCount) data.forEach { row ->
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
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
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
            state: RecyclerView.State
        ) {
            val position = (view.layoutParams as LayoutParams).viewAdapterPosition
            val column = position % spanCount
            // 只给右边和下边留出分割线空间
            outRect.right = if (column < spanCount - 1) dividerSize else 0
            outRect.bottom = dividerSize
        }

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as LayoutParams

                // 画竖线（右边）
                val leftV = child.right + params.rightMargin
                val rightV = leftV + dividerSize
                val topV = child.top - params.topMargin
                val bottomV = child.bottom + params.bottomMargin + dividerSize
                canvas.drawRect(leftV.toFloat(), topV.toFloat(), rightV.toFloat(), bottomV.toFloat(), paint)

                // 画横线（下边）
                val leftH = child.left - params.leftMargin
                val rightH = child.right + params.rightMargin + dividerSize
                val topH = child.bottom + params.bottomMargin
                val bottomH = topH + dividerSize
                canvas.drawRect(leftH.toFloat(), topH.toFloat(), rightH.toFloat(), bottomH.toFloat(), paint)
            }
        }
    }
}
