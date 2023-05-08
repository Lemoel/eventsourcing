package br.com.eventsourcing.core.domain.common

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.repository.CrudRepository

interface AggregateRepository<T : Aggregate> : CrudRepository<T, AggregateId>

interface AggregateInsertableRepository<T> {

    fun insert(aggregate: T): T

    fun update(aggregate: T): T
}

open class AggregateInsertableRepositoryImpl<T : Aggregate>(
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
    private val applicationEventPublisher: ApplicationEventPublisher
) : AggregateInsertableRepository<T> {

    override fun insert(aggregate: T): T =
        aggregate.registeredEvents().takeIf { it.isNotEmpty() }
            ?.onEach {
                applicationEventPublisher.publishEvent(it)
            }
            ?.let {
                jdbcAggregateTemplate.insert(aggregate)
            }
            ?: aggregate

    override fun update(aggregate: T): T =
        aggregate.registeredEvents().takeIf { it.isNotEmpty() }
            ?.onEach { applicationEventPublisher.publishEvent(it) }
            ?.let { jdbcAggregateTemplate.update(aggregate) }
            ?: aggregate
}
