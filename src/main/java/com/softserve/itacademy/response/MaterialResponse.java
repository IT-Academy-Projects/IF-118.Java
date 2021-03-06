package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {

    private Integer id;
    private String name;
    private String description;
    private Integer courseId;
    private Integer ownerId;
    Set<AssignmentResponse> assignments;
}
