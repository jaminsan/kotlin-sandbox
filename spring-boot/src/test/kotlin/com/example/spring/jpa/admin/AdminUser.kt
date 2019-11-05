package com.example.spring.jpa.admin

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class AdminUser(
    @Id val id: Long,
    val name: String,
    val department: String
)