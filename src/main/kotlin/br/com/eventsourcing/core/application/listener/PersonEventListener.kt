package br.com.eventsourcing.core.application.listener

import br.com.eventsourcing.core.domain.person.PersonCreated
import br.com.eventsourcing.core.domain.common.AggregateEventRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PersonEventListener(
    private val repository: AggregateEventRepository
) {
    @EventListener(PersonCreated::class)
    fun listener(event: PersonCreated) {
        repository.insert(
            aggregate = "person",
            event = event
        )
    }

}
