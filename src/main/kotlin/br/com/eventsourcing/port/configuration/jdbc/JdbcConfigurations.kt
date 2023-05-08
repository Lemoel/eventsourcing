package br.com.eventsourcing.port.configuration.jdbc

import br.com.eventsourcing.core.domain.common.AggregateId
import br.com.eventsourcing.core.domain.common.EventId
import org.postgresql.util.PGobject
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration

@Configuration
class JdbcConfigurations : AbstractJdbcConfiguration() {

    override fun jdbcCustomConversions(): JdbcCustomConversions =
        JdbcCustomConversions(
            listOf(
                EventIdWritingConverter(),
                AggregateIdWritingConverter(),
                AggregateIdReadingConverter()
            )
        )

    @WritingConverter
    inner class EventIdWritingConverter : Converter<EventId, PGobject> {

        override fun convert(source: EventId): PGobject =
            PGobject().apply {
                type = "uuid"
                value = source.toUUID().toString()
            }
    }

    @WritingConverter
    inner class AggregateIdWritingConverter : Converter<AggregateId, PGobject> {

        override fun convert(source: AggregateId): PGobject =
            PGobject().apply {
                type = "uuid"
                value = source.toUUID().toString()
            }
    }

    @ReadingConverter
    inner class AggregateIdReadingConverter : Converter<PGobject, AggregateId> {

        override fun convert(source: PGobject): AggregateId? =
            source.value?.let { AggregateId(it) }
    }
}
