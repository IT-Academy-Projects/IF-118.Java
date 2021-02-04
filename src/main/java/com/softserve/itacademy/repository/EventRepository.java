package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
