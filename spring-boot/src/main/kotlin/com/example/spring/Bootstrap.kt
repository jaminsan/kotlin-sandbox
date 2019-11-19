package com.example.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Bootstrap

fun main(args: Array<String>) {
    runApplication<Bootstrap>(*args)
}
