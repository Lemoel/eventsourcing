package br.com.eventsourcing.core.domain.person

import br.com.eventsourcing.core.domain.common.AggregateInsertableRepository
import br.com.eventsourcing.core.domain.common.AggregateRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : AggregateRepository<Person>, AggregateInsertableRepository<Person> {
    fun findByName(name: String): Person?
}
