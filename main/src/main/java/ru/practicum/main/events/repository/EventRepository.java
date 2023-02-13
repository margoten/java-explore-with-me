package ru.practicum.main.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.events.model.Event;

import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Page<Event> findAllByInitiator_IdIs(Long initiatorId, Pageable pageable);

    Event findEventByInitiator_IdAndIdIs(Long initiatorId, Long eventId);

    Set<Event> findAllByIdIn(Set<Long> events);
}
