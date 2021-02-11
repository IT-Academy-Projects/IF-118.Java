package com.softserve.itacademy.response;

import com.softserve.itacademy.projection.UserTinyProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class ChatMessageResponse {

    @EqualsAndHashCode.Exclude
    private UserTinyProjection user;

    @EqualsAndHashCode.Include
    private Integer id;

    @EqualsAndHashCode.Include
    private String content;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedAt;
}
