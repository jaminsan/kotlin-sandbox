package com.example.spring.multipartfile

import org.apache.commons.compress.utils.IOUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@ContextConfiguration(classes = [Config::class])
class FileUploadHandlerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `read excel file`() {
        val excelFile = MockMultipartFile(
            "file",
            "test.xlsx",
            MediaType.APPLICATION_OCTET_STREAM_VALUE,
            IOUtils.toByteArray(ClassPathResource("test.xlsx").inputStream))
        val jsonFile = MockMultipartFile(
            "meta",
            null,
            MediaType.APPLICATION_JSON_VALUE,
            "{\"id\": 1, \"name\": \"test\"}".toByteArray())

        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
            .file(excelFile)
            .file(jsonFile)
        ).andExpect(status().`is`(200))
    }
}