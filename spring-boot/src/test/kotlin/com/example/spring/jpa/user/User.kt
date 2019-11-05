package com.example.spring.jpa.user

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class User(
    @Id val id: Long,
    val name: String
)
