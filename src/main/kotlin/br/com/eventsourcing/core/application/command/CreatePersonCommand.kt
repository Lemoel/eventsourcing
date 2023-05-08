package br.com.eventsourcing.core.application.command

import br.com.eventsourcing.core.domain.person.Person
import br.com.eventsourcing.core.domain.person.personOf


data class CreatePersonCommand(
    val name: String,
)
fun CreatePersonCommand.toPerson(
    name: String,
): Person = personOf(
    name = name,
)