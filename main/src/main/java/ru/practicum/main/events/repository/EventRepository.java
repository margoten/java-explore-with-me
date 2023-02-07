package ru.practicum.main.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.events.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_IdIs(Long initiatorId, Pageable pageable);

    Page<Event> findAllByInitiator_IdInAndState_InAndCategory_IdInAndEventDateBetween(Collection<Long> initiator_id,
                                                                   Collection<Event.EventState> state,
                                                                   Collection<Long> category_id,
                                                                   LocalDateTime rangeStart,
                                                                   LocalDateTime rangeEnd,
                                                                   Pageable pageable

    );

    Event findEventByInitiator_IdAndIdIs(Long initiatorId, Long eventId);

    Set<Event> findAllByIdIn(Set<Long> events);
}
