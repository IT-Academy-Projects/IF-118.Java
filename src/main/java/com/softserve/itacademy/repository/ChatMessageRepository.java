package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.projection.ChatMessageTinyProjection;
import liquibase.pro.packaged.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    Page<ChatMessageTinyProjection> findPaginatedByChatRoomId(Pageable pageable, int chatId);
}
