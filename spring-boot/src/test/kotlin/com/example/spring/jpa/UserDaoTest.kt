package com.example.spring.jpa

import com.example.spring.jpa.user.User
import com.example.spring.jpa.user.UserDao
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [Config::class])
@DataJpaTest
class UserDaoTest @Autowired constructor(
    val userDao: UserDao
) {

    @BeforeEach
    fun beforeAll() {
        userDao.saveAll(listOf(
            User(id = 1, name = "user1"),
            User(id = 2, name = "user2")
        ))
    }

    @AfterEach
    fun afterEach() {
        userDao.deleteAll()
    }

    @Test
    fun `select duplicated record by union all`() {

        val users: List<User> = userDao.findAllUnion()

        users.forEach { println(it.toString()) }
        Assertions.assertEquals(4, users.size)
    }

}