package com.example.db

import com.example.db.record.PersonProfile
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.connectionfactory.ConnectionFactoryUtils
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository

@Repository
class PersonProfileDao(private val databaseClient: DatabaseClient, private val connectionFactory: ConnectionFactory) {

    suspend fun insert(personProfile: PersonProfile): Int =
        databaseClient
            .insert()
            .into(PersonProfile::class.java)
            .using(personProfile)
            .fetch().rowsUpdated().awaitSingle()

    suspend fun insertAll(personProfiles: List<PersonProfile>): Int {
        val connection = ConnectionFactoryUtils.getConnection(connectionFactory).awaitSingle()
        val statement = connection.createStatement(
            """
            insert into
                `person_profile` (`person_profile_id`, `person_id`, `habit`, `weight_kg`, `height_cm`)
            values
                (?personProfileId, ?personId, ?habit, ?weightKg, ?heightCm)
        """.trimIndent()
        )

        personProfiles.forEach {
            statement
                .bind("personProfileId", it.personProfileId)
                .bind("personId", it.personId)
                .bind("habit", it.habit)
                .bind("weightKg", it.weightKg)
                .bind("heightCm", it.heightCm)
                .add()
        }

        return statement.execute().asFlow()
            .toList().map {
                it.rowsUpdated.awaitSingle()
            }.size
    }
}
