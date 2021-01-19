package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.GroupChat;

public interface GroupChatService {

    GroupChat getById(int id);

    GroupChat create();
}
