package com.github.qiaojun2016.ms_docx

import android.content.Context
import org.apache.poi.xwpf.usermodel.*
import java.io.FileInputStream
import java.io.FileOutputStream


object DocxUtil {
    private fun setRowData(values: List<*>, row: XWPFTableRow, bold: Boolean) {
        values.forEachIndexed { index, value ->
            var cell = row.getCell(index)
            if (cell == null) {
                cell = row.addNewTableCell()
            }
            addParagraphTextToCell(cell, value.toString(), bold);
        }
    }

    /**
     * 在表的指定位置插入一列，并将某一行的样式应用到新增行。
     * 通过复制行的来解决坍塌问题
     * @param table XWPFTable
     * @param copyRowIndex Int 需要应用的列样式
     * @param targetRowIndex Int 需要插入列
     * @return XWPFTableRow 返回创建的列。
     */
    private fun insertRow(table: XWPFTable, copyRowIndex: Int, targetRowIndex: Int): XWPFTableRow {
        val targetRow = table.insertNewTableRow(targetRowIndex)
        val copyRow = table.getRow(copyRowIndex)
        targetRow.ctRow.trPr = copyRow.ctRow.trPr
        copyRow.tableCells.forEach { copyCell ->
            val targetCell = targetRow.addNewTableCell();
            targetCell.ctTc.tcPr = copyCell.ctTc.tcPr
        }
        return targetRow;
    }


    private fun addParagraphTextToCell(cell: XWPFTableCell, value: String, bold: Boolean) {
        val paragraph = cell.addParagraph()
        with(paragraph) {
            alignment = ParagraphAlignment.CENTER
            verticalAlignment = TextAlignment.CENTER
            spacingBefore = 4
            spacingAfter = 4

            with(createRun()) {
                isBold = bold
                fontSize = 9
                setText(value, 0)
                addBreak()
            }
        }
        cell.verticalAlignment = XWPFTableCell.XWPFVertAlign.CENTER

    }

    /**
     * 遍历所有表
     * @param document XWPFDocument
     * @param action Function1<XWPFTable, Unit>
     */
    private fun traverseTable(document: XWPFDocument, action: (XWPFTable) -> Unit) {
        val eleIterator = document.bodyElementsIterator;
        while (eleIterator.hasNext()) {
            val docElement = eleIterator.next()
            if ("TABLE" == docElement.elementType.name.uppercase()) {
                docElement.body.tables.forEach(action)
            }
        }
    }

    /**
     * 遍历表中的所有行
     * @param table XWPFTable
     * @param action Function2<Int, XWPFTableRow, Unit>
     */
    private fun traverseRow(table: XWPFTable, action: (Int, XWPFTableRow) -> Unit) {
        var index = 0;
        while (index < table.rows.size) {
            action(index, table.rows[index])
            index++;
        }
    }

    private fun appendRows(
        table: XWPFTable,
        fromIndex: Int,
        valuesList: List<List<String>>
    ) {
        valuesList.forEachIndexed { index, data ->
            val newRow = insertRow(table, fromIndex - 1, fromIndex + index);
            // 设置新增行不占两页
            newRow.isCantSplitRow = true
            setRowData(data.toList(), newRow, false)
        }
    }

    /**
     * 替换表格中的占位符 ${placeHolder}
     * 其中 placeHolder 是content中的键值对
     * @param cell XWPFTableCell 需要替换的单元格
     * @param content Map<String, Any> 键值对字典
     */
    private fun replacePlaceHolderText(cell: XWPFTableCell, dict: Map<String, Any>) {
        cell.paragraphs.forEach { p ->
            p.runs.forEach { run ->
                var text: String? = run.getText(0);
                if (text != null) {
                    println("cell text = $text")
                    val regex = Regex("\\$\\{(\\w+)\\}")
                    val placeNames = regex.findAll(text, 0).toList()
                        .map { it.destructured.component1() }
                    placeNames.forEach { name ->
                        println("placeName = $name")
                        if (dict.keys.contains(name)) {
                            val value: String =
                                dict.getValue(name).toString();
                            text = text?.replace("\${$name}", value);
                        }
                    }
                    run.setText(text, 0)
                }
            }
        }
    }

    fun generateWord(context: Context, input: String, output: String, content: Map<String, Any>) {
        val inputStream = InputStreamSource().getStream(context, input)
        inputStream.use { it ->
            val docx = XWPFDocument(it)
            traverseTable(docx) { table ->
                traverseRow(table) { index, nextRow ->
                    nextRow.tableCells.forEach tableCell@{ cell ->
                        println(cell.text)
                        /// 准备插入行数据
                        if (cell.text.contains("\${list}")) {
                            /// 删除改行，在改行的基础上新增N列
                            table.removeRow(index)
                            appendRows(
                                table,
                                index,
                                content.getValue("list") as List<List<String>>
                            )

                            return@tableCell
                        } else {
                            replacePlaceHolderText(cell, content);
                        }
                    }
                }
            }
            /// 写入word
            FileOutputStream(output).use {
                docx.write(it);
                it.flush()
            }
            Thread.sleep(1000);
        }
    }
}