package com.example.controller.http

data class PostPersonsBatchRequest(
    val persons: List<Person>
) {

    data class Person(
        val name: String,
        val age: Int
    )
}
