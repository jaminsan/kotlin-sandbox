package com.example.spring.jpa.admin

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AdminUserDao : CrudRepository<AdminUser, Long> {

    @Query(
        value = "select id, name, department from admin_user union all select id, name, '' from user",
        countQuery = "select count(1) from (select id from admin_user union all select id from user)",
        nativeQuery = true
    )
    fun findAllAdminUnionFindAllUser(): List<AdminUser>

    @Query(
        value = "select id, name, department from admin_user union all select id, name, '' from user",
        countQuery = "select count(1) from (select id from admin_user union all select id from user)",
        nativeQuery = true
    )
    fun findAllAdminUnionFindAllUserWithPageable(pageable: Pageable): Page<AdminUser>
}