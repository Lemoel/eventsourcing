package br.com.eventsourcing.port.adapter.resources.input

import br.com.eventsourcing.core.application.command.CreatePersonCommand

data class PersonInput(
    val name: String
)

fun PersonInput.toCreatePersonCommand() = CreatePersonCommand(
    name = name
)