package com.example.db.record

data class PersonProfile(
    val personProfileId: String,
    val personId: String,
    val habit: String,
    val weightKg: Int,
    val heightCm: Int
)
