package com.example.spring.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Controller
class FileUploadController {

    data class Meta(val name: String, val comment: String)

    @CrossOrigin(origins = ["http://localhost:8080"])
    @PostMapping("/upload")
    fun multiPartFile(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("meta") meta: Meta
    ): ResponseEntity<Unit> {
        println(file.originalFilename)
        println(meta)

        return ResponseEntity.ok().build()
    }
}