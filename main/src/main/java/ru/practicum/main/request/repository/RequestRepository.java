package ru.practicum.main.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequester_IdIs(Long userId);
    List<Request> findAllByEvent_IdIs(Long eventId);
    List<Request> findAllByEvent_IdIsAndStatusIs(Long eventId, Request.RequestStatus requestStatus);
    List<Request> findAllByEvent_IdIsAndIdIn(Long eventId, List<Long> requestIds);
    Optional<Request> findAllByEvent_IdIsAndRequester_IdIs(Long userId, Long eventId);
}
