package br.com.eventsourcing.port.adapter.resources

import br.com.eventsourcing.core.application.usecase.PersonUseCase
import br.com.eventsourcing.port.adapter.resources.input.PersonInput
import br.com.eventsourcing.port.adapter.resources.input.toCreatePersonCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/people")
class PersonResource(
    val personUseCase: PersonUseCase
) {
    @PostMapping
    fun createAgreement(@RequestBody personInput: PersonInput): ResponseEntity<String> {
        personUseCase.execute(personInput.toCreatePersonCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(personInput.name)
    }
}
