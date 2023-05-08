package br.com.eventsourcing.core.application.usecase

import br.com.eventsourcing.core.application.command.CreatePersonCommand
import br.com.eventsourcing.core.application.command.toPerson
import br.com.eventsourcing.core.domain.person.Person
import br.com.eventsourcing.core.domain.person.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonUseCase(
    val personRepository: PersonRepository,
) {
    fun execute(command: CreatePersonCommand) {
        command.toPerson(command.name)
            ?.let { insert(it) }
    }

    private fun insert(person: Person): Person =
        person.let { personRepository.insert(person) }
}
