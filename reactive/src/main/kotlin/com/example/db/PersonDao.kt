package com.example.db

import com.example.db.record.Person
import com.example.db.record.PersonWithProfile
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.*
import org.springframework.data.r2dbc.connectionfactory.ConnectionFactoryUtils
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.awaitFirst
import org.springframework.data.r2dbc.core.awaitFirstOrNull
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
class PersonDao(private val databaseClient: DatabaseClient, private val connectionFactory: ConnectionFactory) {

    suspend fun find(id: String): Person? =
        databaseClient
            .select()
            .from(Person::class.java)
            .matching(
                where("person_id").`is`(id)
            )
            .fetch()
            .awaitFirst()

    suspend fun insert(person: Person): Int =
        databaseClient
            .insert()
            .into(Person::class.java)
            .using(person)
            .fetch()
            .rowsUpdated()
            .awaitSingle()

    suspend fun insertAll(persons: List<Person>): Int {
        val connection = ConnectionFactoryUtils.getConnection(connectionFactory).awaitSingle()
        val statement = connection.createStatement(
            """
                insert into
                    `person` (`person_id`, `name`, `age`)
                values
                    (?personId, ?name, ?age)
            """.trimIndent()
        )

        persons.forEach {
            statement
                .bind("personId", it.personId)
                .bind("name", it.name)
                .bind("age", it.age)
                .add()
        }

        return statement.execute().asFlow()
            .toList().map {
                it.rowsUpdated.awaitSingle()
            }.size
    }

    fun insertMono(person: Person): Mono<Int> =
        databaseClient
            .insert()
            .into(Person::class.java)
            .using(person)
            .fetch()
            .rowsUpdated()
            .flatMap {
                databaseClient.insert()
                    .into(Person::class.java).using(person.copy(personId = UUID.randomUUID().toString()))
                    .fetch()
                    .rowsUpdated()
            }

    fun findMono(id: String): Mono<Person> =
        databaseClient
            .select()
            .from(Person::class.java)
            .fetch()
            .first()

    suspend fun findRawSql(id: String): Person? =
        databaseClient
            .execute("select * from person where person_id = :personId")
            .bind("personId", id)
            .`as`(Person::class.java)
            .fetch()
            .awaitFirstOrNull()

    suspend fun findWithProfile(id: String): PersonWithProfile? {
        val selectSql = """
                select 
                    *
                from 
                    person p
                join 
                    person_profile pp using (person_id)
                where
                    person_id = :personId 
            """.trimIndent()
        return databaseClient
            .execute(
                selectSql
            )
            .bind("personId", id)
            .`as`(PersonWithProfile::class.java)
            .fetch()
            .awaitFirstOrNull()
}
}
