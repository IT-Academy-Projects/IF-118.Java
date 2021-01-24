package com.softserve.itacademy.response;

import com.softserve.itacademy.projection.UserTinyProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageResponse {

    private UserTinyProjection user;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
