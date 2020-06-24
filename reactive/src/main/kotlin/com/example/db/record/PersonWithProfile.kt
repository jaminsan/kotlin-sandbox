package com.example.db.record

data class PersonWithProfile(
    val personId: String,
    val name: String,
    val age: Int,
    val personProfileId: String,
    val habit: String,
    val weightKg: Int
)
