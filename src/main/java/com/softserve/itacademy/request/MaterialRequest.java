package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequest {

    private String name;
    private int courseId;
    private int ownerId;
    private String description;

}
