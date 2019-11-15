package com.example.spring.multipartfile

import com.monitorjbl.xlsx.StreamingReader
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.util.stream.Collectors
import java.util.stream.StreamSupport

@Configuration
class Config {

    data class Meta(val id: Long, val name: String)

    @Controller
    class FileUploadHandler {

        @PostMapping(
            "/uploadFile",
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE]
        )
        fun uploadFile(@RequestPart("file") file: MultipartFile, @RequestPart("meta") meta: Meta): ResponseEntity<Void> {
            buildWorkbook(file).use { workBook ->
                validateFileData(workBook)

                return ResponseEntity.ok().build()
            }
        }

        private fun buildWorkbook(file: MultipartFile): Workbook {
            return StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(file.inputStream)
        }

        private fun validateFileData(workBook: Workbook) {
            val isValid = { s: String -> s.startsWith("valid") }
            val collect = StreamSupport.stream(workBook.getSheetAt(0).spliterator(), false)
                .filter {
                    println("rowNum=${it.rowNum}, cellValue=${it.getCell(0)}")
                    !isValid(it.getCell(0).stringCellValue)
                }
                .limit(2).collect(Collectors.toList())
            println("invalid records count ${collect.size}")
        }
    }
}