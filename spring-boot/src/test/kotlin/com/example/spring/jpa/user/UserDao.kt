package com.example.spring.jpa.user

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserDao : CrudRepository<User, Long> {

    @Query(
        value = "select * from user union all select * from user",
        countQuery = "select count(1) from (select id from user union all select id from user)",
        nativeQuery = true
    )
    fun findAllUnion(): List<User>
}
