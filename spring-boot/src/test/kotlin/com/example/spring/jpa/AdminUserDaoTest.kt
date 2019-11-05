package com.example.spring.jpa

import com.example.spring.jpa.admin.AdminUser
import com.example.spring.jpa.admin.AdminUserDao
import com.example.spring.jpa.user.User
import com.example.spring.jpa.user.UserDao
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [Config::class])
@DataJpaTest
class AdminUserDaoTest @Autowired constructor(
    val userDao: UserDao,
    val adminUserDao: AdminUserDao
) {

    @BeforeEach
    fun beforeAll() {
        userDao.saveAll(listOf(
            User(id = 1, name = "user1"),
            User(id = 2, name = "user2"),
            User(id = 5, name = "user5")
        ))

        adminUserDao.saveAll(listOf(
            AdminUser(id = 1, name = "admin1", department = "Sales"),
            AdminUser(id = 2, name = "admin2", department = "IS"),
            AdminUser(id = 3, name = "admin3", department = "CS")
        ))
    }

    @AfterEach
    fun afterEach() {
        userDao.deleteAll()
        adminUserDao.deleteAll()
    }

    @Test
    fun `findAllAdminUnionFindAllUser return all record of admin_user and user`() {
        val adminUsers = adminUserDao.findAllAdminUnionFindAllUser()

        //AdminUser(id=1, name=admin1, department=Sales)
        //AdminUser(id=2, name=admin2, department=IS)
        //AdminUser(id=3, name=admin3, department=CS)
        //AdminUser(id=1, name=admin1, department=Sales)
        //AdminUser(id=2, name=admin2, department=IS)
        //AdminUser(id=5, name=user5, department=)
        adminUsers.forEach(::println)
        assertEquals(6, adminUsers.size)

        assertAll(listOf("user1", "admin1").map { name ->
            { assertTrue(adminUsers.any { u -> u.name == name }) }
        })
    }

    @Test
    fun `findAllAdminUnionFindAllUserWithPageable return all record of admin_user and user`() {
        val adminUsers = adminUserDao.findAllAdminUnionFindAllUserWithPageable(PageRequest.of(0, 5))

        //AdminUser(id=1, name=admin1, department=Sales)
        //AdminUser(id=2, name=admin2, department=IS)
        //AdminUser(id=3, name=admin3, department=CS)
        //AdminUser(id=1, name=admin1, department=Sales)
        //AdminUser(id=2, name=admin2, department=IS)
        //AdminUser(id=5, name=user5, department=)
        adminUsers.forEach(::println)
        assertEquals(6, adminUsers.size)

        assertAll(listOf("user1", "admin1").map { name ->
            { assertTrue(adminUsers.any { u -> u.name == name }) }
        })
    }
}