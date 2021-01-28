package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    Page<ChatMessage> findPaginatedByChatRoomId(Pageable pageable, int chatId);
}
