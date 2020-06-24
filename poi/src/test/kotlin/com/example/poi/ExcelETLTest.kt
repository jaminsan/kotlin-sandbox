package com.example.poi

import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class ExcelETLTest {

    @BeforeEach
    fun setUp() {
        val path = "src/test/resources/test.xlsx"
        File(path).outputStream().use { os ->
            SXSSFWorkbook().use { wb ->
                val sheet = wb.createSheet("test")

                repeat(10) { rowIndex ->
                    val row = sheet.createRow(rowIndex)
                    val c = row.createCell(0)
                    c.setCellValue("hoge$rowIndex")
                }
                wb.write(os)
            }
        }

        File(path).inputStream().use { ins ->
            SXSSFWorkbook().use { wb ->
            }
        }
    }

    @Test
    fun `read excel file then map its content to byte array output stream`() {

    }
}