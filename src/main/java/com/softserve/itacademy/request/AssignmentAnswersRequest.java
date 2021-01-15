package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssignmentAnswersRequest {

    private Integer assignmentId;
    private Integer ownerId;
}
