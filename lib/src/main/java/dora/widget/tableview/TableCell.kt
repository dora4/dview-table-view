package dora.widget.tableview

import android.graphics.Color

/**
 * 单元格模型：可定制文字、大小、颜色、粗/斜体、内边距。
 */
data class TableCell(
    val text: String,
    val textSizeSp: Float = 14f,
    val textColor: Int = Color.BLACK,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val paddingDp: Int = 8,
    val backgroundColor: Int = Color.TRANSPARENT
)

