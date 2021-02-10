package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MaterialResponse {

    @EqualsAndHashCode.Include
    private Integer id;

    private String name;
    private String description;
    private Integer courseId;
    private Integer ownerId;
    private Set<AssignmentResponse> assignments;
}
