package br.com.eventsourcing.core.domain.common

import java.time.Instant
import java.time.Instant.now

interface AggregateEvent

open class AggregateCreated<T : Aggregate>(
    open val id: EventId = EventId(),
    open val aggregate: T,
    open val type: EventType,
    open val occurredOn: Instant = now()
) : AggregateEvent

open class AggregateUpdated<T : Aggregate>(
    open val id: EventId = EventId(),
    open val original: T,
    open val updated: T,
    open val type: EventType,
    open val occurredOn: Instant = now()
) : AggregateEvent
