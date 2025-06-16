package dora.widget.tableview

/**
 * 单元格模型：可定制文字、大小、颜色、粗/斜体、内边距。
 */
data class TableCell(
    val text: String,
    val textSizeSp: Float = 14f,
    val textColor: Int = 0xFF000000.toInt(),
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val paddingDp: Int = 8
)
