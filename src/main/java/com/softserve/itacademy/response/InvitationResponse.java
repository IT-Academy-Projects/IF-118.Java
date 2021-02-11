package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationResponse {

    private Integer id;
    private Boolean approved;
    private String courseOrGroup;
    private String link;
    private Integer courseOrGroupId;
    private String code;
}
