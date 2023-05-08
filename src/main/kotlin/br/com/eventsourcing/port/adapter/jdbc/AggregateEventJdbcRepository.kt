package br.com.eventsourcing.port.adapter.jdbc

import br.com.eventsourcing.core.domain.common.AggregateCreated
import br.com.eventsourcing.core.domain.common.AggregateEventRepository
import br.com.eventsourcing.core.domain.common.AggregateId
import br.com.eventsourcing.core.domain.common.AggregateUpdated
import br.com.eventsourcing.core.domain.common.EventId
import br.com.eventsourcing.core.domain.common.EventType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.ZoneOffset.UTC

@Repository
class AggregateEventJdbcRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val objectMapperWithNulls: ObjectMapper
) : AggregateEventRepository {

    fun <T : Any> getDifferences(originalInvoice: T?, updatedInvoice: T?): Any? {
        val originalInvoiceMap = originalInvoice?.let { objectMapperWithNulls.convertValue<Map<String, Any?>>(it) }
        val updatedInvoiceMap = updatedInvoice?.let { objectMapperWithNulls.convertValue<Map<String, Any?>>(it) }

        return updatedInvoiceMap
            ?.entries
            ?.filter { it.value != originalInvoiceMap?.get(it.key) }
            ?.associate { it.key to it.value }
            ?.mapValues { entry ->
                entry
                    .value
                    ?.takeIf { it is Map<*, *> }
                    ?.let { getDifferences(originalInvoiceMap?.get(entry.key), it) }
                    ?: entry.value
            }
    }

    override fun insert(
        aggregate: String,
        event: AggregateCreated<*>
    ) {
        insert(
            aggregate,
            event.id,
            event.aggregate.id,
            event.type,
            event.occurredOn,
            objectMapper.writeValueAsString(event.aggregate)
        )
    }

    override fun insert(
        aggregate: String,
        event: AggregateUpdated<*>
    ) {
        insert(
            aggregate,
            event.id,
            event.updated.id,
            event.type,
            event.occurredOn,
            objectMapperWithNulls.writeValueAsString(
                getDifferences(event.original, event.updated)
            )
        )
    }

    fun delete(
        aggregate: String,
        id: EventId
    ) {
        jdbcTemplate.update(
            "DELETE FROM ${aggregate}_event WHERE id = ?",
            id.toUUID()
        )
    }

    fun insert(
        aggregate: String,
        id: EventId,
        aggregateId: AggregateId,
        eventType: EventType,
        occurredOn: Instant,
        additionalData: Any?
    ) {
        jdbcTemplate.update(
            getInsert(aggregate),
            id.toUUID(),
            aggregateId.toUUID(),
            eventType.name,
            occurredOn.atOffset(UTC),
            additionalData?.toString()
        )
    }

    private fun getInsert(aggregate: String): String =
        "INSERT INTO ${aggregate}_event(id, aggregate_id, type, occurred_on, additional_data) " +
            "VALUES (?, ?, ?::${aggregate}_EVENT_TYPE, ?, ?::JSON)"
}
