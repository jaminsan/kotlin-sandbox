package com.example.service

import com.example.controller.http.PostPersonsBatchRequest
import com.example.db.PersonDao
import com.example.db.PersonProfileDao
import com.example.db.record.Person
import com.example.db.record.PersonProfile
import com.example.db.record.PersonWithProfile
import io.r2dbc.spi.ConnectionFactory
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import reactor.core.publisher.Mono
import java.lang.RuntimeException
import java.util.*

@Service
class PersonService(
    private val personDao: PersonDao,
    private val personProfileDao: PersonProfileDao,
    private val connectionFactory: ConnectionFactory
) {
    suspend fun registerPerson(name: String, age: Int): Person? {
        val txM = R2dbcTransactionManager(connectionFactory)
        val operator = TransactionalOperator.create(txM)

        val person = Person(personId = UUID.randomUUID().toString(), name = name, age = age)

        return operator.executeAndAwait {
            personDao.insert(person)

            val personProfile = PersonProfile(
                personId = person.personId,
                personProfileId = UUID.randomUUID().toString(),
                habit = "walking",
                weightKg = 50,
                heightCm = 170
            )
            personProfileDao.insert(personProfile)

            if (person.name == "jaminsan") {
                throw RuntimeException("error in transaction")
            }

            personDao.find(person.personId)
        }
    }

    suspend fun registerPersons(persons: List<PostPersonsBatchRequest.Person>): Int {
        val txM = R2dbcTransactionManager(connectionFactory)
        val operator = TransactionalOperator.create(txM)

        val personRecords = persons.map {
            Person(
                personId = UUID.randomUUID().toString(),
                name = it.name,
                age = it.age
            )
        }

        val personProfileRecords = personRecords.map {
            PersonProfile(
                personProfileId = UUID.randomUUID().toString(),
                personId = it.personId,
                habit = "habit-${it.name}",
                weightKg = 50,
                heightCm = 170
            )
        }

        return operator.executeAndAwait {
            val personsCount = personDao.insertAll(personRecords)

            personProfileDao.insertAll(personProfileRecords)

            if (personProfileRecords.any { it.habit.contains("jaminsan") }) {
                throw Exception("profile error")
            }

            personsCount
        } ?: throw Exception("registerPersons failed")
    }

    fun registerPersonMono(name: String, age: Int): Mono<Person> {
        val person = Person(UUID.randomUUID().toString(), name, age)

        return personDao.insertMono(person)
            .flatMap {
                personDao.findMono(person.personId)
            }
    }

    suspend fun findPersonRawSql(id: String): Person? =
        personDao.findRawSql(id)

    suspend fun findPersonWithProfile(id: String): PersonWithProfile? =
        personDao.findWithProfile(id)
}
