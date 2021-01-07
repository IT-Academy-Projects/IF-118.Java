package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {

    private Integer id;
    private Integer ownerId;
    private String name;
    private Integer courseId;
    private String description;

}
