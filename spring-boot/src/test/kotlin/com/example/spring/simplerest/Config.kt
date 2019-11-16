package com.example.spring.simplerest

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

@Configuration
class Config {

    data class Message(val from: String, val to: String, val greet: String)

    data class Reply(val message: String)

    @Controller
    class Ping {

        @PostMapping("/ping")
        @ResponseBody
        fun ping(@RequestBody message: Message): Reply {
            return Reply("${message.greet} ${message.from}")
        }

    }
}