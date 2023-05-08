package br.com.eventsourcing.core.domain.person

import br.com.eventsourcing.core.domain.common.Aggregate
import br.com.eventsourcing.core.domain.common.AggregateId
import org.springframework.data.annotation.Id

data class Person(
    @Id override val id: AggregateId,
    val name: String,
) : Aggregate()

fun personOf(
    name: String
): Person = Person(
    id = AggregateId(), name = name
).apply {
    addEvent { PersonCreated(aggregate = it) }
}