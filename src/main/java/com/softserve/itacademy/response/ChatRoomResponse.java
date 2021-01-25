package com.softserve.itacademy.response;

import com.softserve.itacademy.entity.ChatRoom;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomResponse {

    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ChatRoom.ChatType type;
}
