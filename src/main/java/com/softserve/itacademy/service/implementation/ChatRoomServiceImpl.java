package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.ChatRoomRepository;
import com.softserve.itacademy.service.ChatRoomService;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    public ChatRoom getById(int id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new NotFoundException("Message with id " + id + " not found"));
    }

    @Override
    public ChatRoom create() {
        ChatRoom chat = new ChatRoom();
        chat = chatRoomRepository.save(chat);
        return chat;
    }

}
