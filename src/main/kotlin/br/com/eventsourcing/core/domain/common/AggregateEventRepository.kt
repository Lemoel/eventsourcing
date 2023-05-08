package br.com.eventsourcing.core.domain.common

interface AggregateEventRepository {

    fun insert(
        aggregate: String,
        event: AggregateCreated<*>
    )

    fun insert(
        aggregate: String,
        event: AggregateUpdated<*>
    )
}
