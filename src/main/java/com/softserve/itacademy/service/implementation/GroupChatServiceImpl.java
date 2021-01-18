package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.GroupChat;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.GroupChatRepository;
import com.softserve.itacademy.service.GroupChatService;
import org.springframework.stereotype.Service;

@Service
public class GroupChatServiceImpl implements GroupChatService {

    private final GroupChatRepository groupChatRepository;

    public GroupChatServiceImpl(GroupChatRepository groupChatRepository) {
        this.groupChatRepository = groupChatRepository;
    }

    @Override
    public GroupChat getById(int id) {
        return groupChatRepository.findById(id).orElseThrow(NotFoundException::new);
    }

}
