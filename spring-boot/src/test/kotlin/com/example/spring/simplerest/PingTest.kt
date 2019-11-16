package com.example.spring.simplerest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@ContextConfiguration(classes = [Config::class])
class PingTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun ping() {
        val body = Config.Message("from", "to", "Hello")

        mockMvc
            .perform(MockMvcRequestBuilders
                .post("/ping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(body))
            ).andExpect(status().isOk)

        println(mockMvc.post("/ping", body)
            .andReturn().resolvedException?.message)
    }
}