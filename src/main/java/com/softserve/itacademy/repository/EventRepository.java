package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findPaginatedByRecipientId(int recipientId, Pageable pageable);

}
