package com.softserve.itacademy.security;

import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.ChatRoomRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.principal.UserPrincipal;
import org.springframework.transaction.annotation.Transactional;

public class AccessManager {

    private final GroupRepository groupRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private static final String USER_ID_NOT_FOUND = "User with such id was not found";

    public AccessManager(GroupRepository groupRepository, ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean isAllowedToChat(Object principal, Integer chatId) {

        UserPrincipal userPrincipal = (UserPrincipal) principal;
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NotFoundException(USER_ID_NOT_FOUND));

        ChatRoom chat = chatRoomRepository.findById(chatId).orElseThrow(() -> new NotFoundException("Chat with id " + chatId + " not found"));

        if (chat.getType() == ChatRoom.ChatType.GROUP) {

            Group group = groupRepository.findByChatRoomId(chatId).orElseThrow(() -> new NotFoundException("Group with Chat Id " + chatId + " not found"));
            return isGroupMember(user, group);
        }

        return false;
    }

    private boolean isGroupMember(User user, Group group) {

        if (user.getId().equals(group.getOwnerId())) {
            return true;
        }

        for (var usr : group.getUsers()) {
            if (usr.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
