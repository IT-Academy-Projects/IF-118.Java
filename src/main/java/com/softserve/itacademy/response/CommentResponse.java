package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Integer id;
    private String created_at;
    private String updated_at;
    private String message;
    private Integer ownerId;
    private Integer materialId;
    private Boolean isPrivate;
    private Boolean disabled;
}
