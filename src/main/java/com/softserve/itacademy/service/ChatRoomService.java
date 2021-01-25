package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.ChatRoom;

public interface ChatRoomService {

    ChatRoom getById(int id);

    ChatRoom create();
}
