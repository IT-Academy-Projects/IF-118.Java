package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRequest {

    private String email;
    private LocalDateTime expirationDate;
    private Boolean approved;
    private String link;
    private int userId;
    private int courseId;
    private int groupId;

}
