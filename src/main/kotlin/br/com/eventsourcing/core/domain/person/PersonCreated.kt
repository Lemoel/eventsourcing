package br.com.eventsourcing.core.domain.person

import br.com.eventsourcing.core.domain.common.AggregateCreated
import br.com.eventsourcing.core.domain.common.EventId
import br.com.eventsourcing.core.domain.common.EventType.PERSON_CREATED
import java.time.Instant

data class PersonCreated(
    override val id: EventId = EventId(),
    override val aggregate: Person,
    override val occurredOn: Instant = Instant.now()
) : AggregateCreated<Person>(
    id = id,
    aggregate = aggregate,
    type = PERSON_CREATED,
    occurredOn = occurredOn
) {

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean =
        other is PersonCreated && id == other.id && aggregate == other.aggregate
}
