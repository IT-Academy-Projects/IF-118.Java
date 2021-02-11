package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationRequest {

    private String email;
    private int userId;
    private int courseId;
    private int groupId;
    private int ownerId;

}
