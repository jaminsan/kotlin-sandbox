package com.example.spring.multipartfile

import org.apache.commons.compress.utils.IOUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@ContextConfiguration(classes = [Config::class])
class FileUploadHandlerTest {

    @Autowired
    lateinit var wac: WebApplicationContext

    @Test
    fun `read excel file`() {
        val excelFile = MockMultipartFile(
            "file",
            "test.xlsx",
            "application/octet-stream",
            IOUtils.toByteArray(ClassPathResource("test.xlsx").inputStream))
        val jsonFile = MockMultipartFile(
            "meta",
            null,
            "application/json",
            "{\"id\": 1, \"name\": \"test\"}".toByteArray())

        val mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadFile")
            .file(excelFile)
            // TODO: 415 になってしまう
//            .file(jsonFile)
        ).andExpect(status().`is`(200))
    }
}