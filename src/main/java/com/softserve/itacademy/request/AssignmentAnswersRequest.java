package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//TODO move the request package under controller package as it has direct relation to it. response package move as well
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssignmentAnswersRequest {

    private Integer assignmentId;
    private Integer ownerId;
}
