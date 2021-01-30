package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AssignmentResponse {

    @EqualsAndHashCode.Include
    private Integer id;
    private String name;
    private String description;
    private Integer materialId;
    Set<AssignmentAnswersResponse> assignmentAnswers;
}