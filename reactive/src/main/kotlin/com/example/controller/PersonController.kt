package com.example.controller

import com.example.controller.http.PostPersonsBatchRequest
import com.example.controller.http.PostPersonsRequest
import com.example.db.record.Person
import com.example.db.record.PersonWithProfile
import com.example.service.PersonService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class PersonController(private val personService: PersonService) {

    @PostMapping("/persons")
    suspend fun postPersons(@RequestBody body: PostPersonsRequest): String? {
        return personService.registerPerson(body.name, body.age)?.personId
    }

    @PostMapping("/personsBatch")
    suspend fun postPersonsBatch(@RequestBody body: PostPersonsBatchRequest): Int {
        return personService.registerPersons(body.persons)
    }

    @PostMapping("/personsMono")
    fun postPersonsMono(@RequestBody body: PostPersonsRequest): Mono<String> {
        return personService.registerPersonMono(body.name, body.age).map { it.personId }
    }

    @GetMapping("/personsRawSql/{personId}")
    suspend fun getPersonsRawSql(@PathVariable personId: String): Person? =
        personService.findPersonRawSql(personId)

    @GetMapping("/personsWithProfile/{personId}")
    suspend fun getPersonsWithProfile(@PathVariable personId: String): PersonWithProfile? =
        personService.findPersonWithProfile(personId)
}
