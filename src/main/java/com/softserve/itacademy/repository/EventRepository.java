package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "select * from events e " +
            "join events_recipients er on e.id = er.event_id " +
            "where er.recipient_id = :recipientId", nativeQuery = true)
    Page<Event> findPaginatedByRecipientId(int recipientId, Pageable pageable);

}
